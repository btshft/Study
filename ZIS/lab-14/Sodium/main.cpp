#include <vector>
#include <string>
#include <iostream>
#include <iomanip>
#include <sstream>

#include <sodium.h>

using std::setw;
using std::hex;
using std::setfill;
using std::uppercase;
using std::string;
using std::cout;
using std::ostringstream;
using std::endl;

typedef unsigned char byte;

std::vector<byte> bytes(string& str){
    std::vector<byte> output(str.length());
    std::transform(str.begin(), str.end(), output.begin(), [](char c) {
        return static_cast<byte>(c);
    });

    return output;
}

string text(std::vector<byte>& data){
    return string(data.begin(), data.end());
}

template <typename TIterator>
string hex(TIterator first, TIterator last){
    ostringstream ss;
    ss << "0x" << hex << setfill('0') << uppercase;

    while (first != last)
        ss << setw(2) << static_cast<int>(*first++);

    return ss.str();
}

class Hash {
public:
    static std::vector<byte> SHA256(std::vector<byte>& data) {
        std::vector<byte> result(crypto_hash_sha256_BYTES);
        crypto_hash_sha256(&result[0], &data[0], data.size());
        return result;
    }

    static std::vector<byte> SHA512(std::vector<byte>& data){
        std::vector<byte> result(crypto_hash_sha512_BYTES);
        crypto_hash_sha512(&result[0], &data[0], data.size());
        return result;
    }
};

class SecretBox {
public:
    static std::vector<byte> Encrypt(std::vector<byte>& data, std::vector<byte>& key, std::vector<byte>& nonce){
        std::vector<byte> encrypted(crypto_secretbox_MACBYTES + data.size());
        crypto_secretbox_easy(&encrypted[0], &data[0], data.size(), &nonce[0], &key[0]);
        return encrypted;
    }

    static std::vector<byte> Decrypt(std::vector<byte>& data, std::vector<byte>& key, std::vector<byte>& nonce){
        std::vector<byte> decrypted(data.size() - crypto_secretbox_MACBYTES);
        crypto_secretbox_open_easy(&decrypted[0], &data[0], data.size(), &nonce[0], &key[0]);
        return decrypted;
    }
};

class SecretGenerator {
public:
    static std::vector<byte> CreateSecretBoxKey() {
        std::vector<byte> key(crypto_secretbox_KEYBYTES);
        crypto_secretbox_keygen(&key[0]);
        return key;
    }

    static std::vector<byte> CreateSecretBoxNonce(){
        std::vector<byte> nonce(crypto_secretbox_NONCEBYTES);
        randombytes_buf(&nonce[0], nonce.size());
        return nonce;
    }
};

class DigitalSigner {
public:
    std::vector<byte> SecretKey;
    std::vector<byte> PublicKey;

public:
    DigitalSigner() {
        SecretKey = std::vector<byte>(crypto_sign_SECRETKEYBYTES);
        PublicKey = std::vector<byte>(crypto_sign_PUBLICKEYBYTES);

        crypto_sign_keypair(&PublicKey[0], &SecretKey[0]);
    }

public:

    std::vector<byte> Sign(std::vector<byte>& data) const {
        std::vector<byte> signature(crypto_sign_BYTES);
        crypto_sign_detached(&signature[0], 0x00, &data[0], data.size(), &SecretKey[0]);
        return signature;
    }

    bool Validate(std::vector<byte>&data, std::vector<byte>& signature) const {
        return crypto_sign_verify_detached(&signature[0], &data[0], data.size(), &PublicKey[0]) == 0;
    }
};


int main(int argc, char* argv[])
{
    string message("Привет Мир!");
    auto messageBytes = bytes(message);
    auto sha256Msg = Hash::SHA256(messageBytes);
    auto sha512Msg = Hash::SHA512(messageBytes);

    cout << "Вычисление SHA256 / SHA512" << endl;
    cout << "Сообщение: " << message << endl;
    cout << "SHA256: " << hex(sha256Msg.begin(), sha256Msg.end()) << endl;
    cout << "SHA512: " << hex(sha512Msg.begin(), sha512Msg.end()) << endl << endl;

    auto key = SecretGenerator::CreateSecretBoxKey();
    auto nonce = SecretGenerator::CreateSecretBoxNonce();
    std::vector<byte> encrypted = SecretBox::Encrypt(messageBytes, key, nonce);
    std::vector<byte> decrypted = SecretBox::Decrypt(encrypted, key, nonce);

    cout << "Шифрование crypto_secretbox: " << endl;
    cout << "Ключ: " << hex(key.begin(), key.end()) << endl;
    cout << "Нонс: " << hex(nonce.begin(), nonce.end()) << endl;
    cout << "Сообщение: " << message << endl;
    cout << "Зашифрованное сообщение: " << hex(encrypted.begin(), encrypted.end()) << endl;
    cout << "Расшифрованное сообщение: " << text(decrypted) << endl << endl;

    DigitalSigner signer{};
    auto signature = signer.Sign(messageBytes);

    cout << "Цифровая подпись сообщения: " << endl;
    cout << "Сообщение: " << message << endl;
    cout << "Публичный ключ: " << hex(signer.PublicKey.begin(), signer.PublicKey.end()) << endl;
    cout << "Приватный ключ: " << hex(signer.SecretKey.begin(), signer.SecretKey.end()) << endl;
    cout << "Подпись: " << hex(signature.begin(), signature.end()) << endl;
    cout << "Подпись корректна: " << (signer.Validate(messageBytes, signature) ? "Да" : "Нет") << endl;
}