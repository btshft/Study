// g++ -g3 -ggdb -O0 -DDEBUG -I/usr/include/cryptopp Driver.cpp -o Driver.exe -lcryptopp -lpthread
// g++ -g -O2 -DNDEBUG -I/usr/include/cryptopp Driver.cpp -o Driver.exe -lcryptopp -lpthread

#include <cryptopp/osrng.h>
using CryptoPP::AutoSeededRandomPool;

#include <iostream>
using std::cout;
using std::cerr;
using std::endl;

#include <string>
using std::string;

#include <sstream>
using std::ostringstream;

#include <iomanip>
using std::setw;
using std::hex;
using std::setfill;
using std::uppercase;

#include <vector>
using std::vector;

#include <cstdlib>
using std::exit;

#include <cryptopp/cryptlib.h>
using CryptoPP::Exception;

#include <cryptopp/hex.h>
using CryptoPP::HexEncoder;
using CryptoPP::HexDecoder;

#include <cryptopp/filters.h>
using CryptoPP::StringSink;
using CryptoPP::StringSource;
using CryptoPP::StreamTransformationFilter;

using CryptoPP::ArraySink;
using CryptoPP::ArraySource;

using CryptoPP::Redirector;

#include <cryptopp/aes.h>
using CryptoPP::AES;

#include <cryptopp/modes.h>
using CryptoPP::CFB_Mode;

#include <cryptopp/files.h>
using CryptoPP::FileSink;

#include <cryptopp/salsa.h>
using CryptoPP::Salsa20;

#include <cryptopp/blowfish.h>
using CryptoPP::Blowfish;

template <typename TAlgorithm>
class Cipher {

public:
    static std::vector<byte> encrypt(std::vector<byte>& data, std::vector<byte>& key, std::vector<byte>& iv)  {
        typename TAlgorithm::Encryption encryption;
        encryption.SetKeyWithIV(key.data(), key.size(), iv.data(), iv.size());

        std::vector<byte> encrypted;
        encrypted.resize(data.size() + AES::BLOCKSIZE);

        ArraySink es(&encrypted[0], encrypted.size());
        ArraySource(data.data(), data.size(), true,
                    new StreamTransformationFilter(encryption, new Redirector(es)));

        encrypted.resize(es.TotalPutLength());

        return encrypted;
    }

    static std::vector<byte> decrypt(std::vector<byte>& data, std::vector<byte>& key, std::vector<byte>& iv) {
        typename TAlgorithm::Decryption decryption;
        decryption.SetKeyWithIV(key.data(), key.size(), iv.data(), iv.size());

        std::vector<byte> decrypted;
        decrypted.resize(data.size());

        ArraySink ds(&decrypted[0], decrypted.size());
        ArraySource(data.data(), data.size(), true,
                    new StreamTransformationFilter(decryption, new Redirector(ds)));

        decrypted.resize(ds.TotalPutLength());

        return decrypted;
    }
};

class RandomGenerator {
public:
    static std::vector<byte> GenerateBlock(const size_t & blockSize){
        AutoSeededRandomPool rnd;

        std::vector<byte> block(blockSize);
        rnd.GenerateBlock(&block[0], blockSize);

        return block;
    }
};

template <typename TAlgorithm>
class SecretGenerator {
public:
    static std::vector<byte> GenerateIv() {
        auto iv_size = TAlgorithm::IV_LENGTH;
        if (iv_size == 0)
            iv_size = TAlgorithm::BLOCKSIZE;

        return RandomGenerator::GenerateBlock(iv_size);
    }

    static std::vector<byte> GenerateKey() {
        return RandomGenerator::GenerateBlock(TAlgorithm::DEFAULT_KEYLENGTH);
    }
};

template <typename TIterator>
string hex(TIterator first, TIterator last){
    ostringstream ss;
    ss << "0x" << hex << setfill('0') << uppercase;

    while (first != last)
        ss << setw(2) << static_cast<int>(*first++);

    return ss.str();
}

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

int main(int argc, char* argv[])
{
    auto iv = SecretGenerator<Blowfish>::GenerateIv();
    auto key = SecretGenerator<Blowfish>::GenerateKey();
    string message("Hello World");

    auto msgBytes = bytes(message);
    auto encrypted = Cipher<CFB_Mode<Blowfish>>::encrypt(msgBytes, key, iv);
    auto decrypted = Cipher<CFB_Mode<Blowfish>>::decrypt(encrypted, key, iv);

    cout << "IV: " << hex(iv.begin(), iv.end()) << endl;
    cout << "KEY: " << hex(key.begin(), key.end()) << endl;
    cout << "Message: " << message << endl;
    cout << "Encrypted: " << hex(encrypted.begin(), encrypted.end()) << endl;
    cout << "Decrypted: " << text(decrypted) << endl;
}

