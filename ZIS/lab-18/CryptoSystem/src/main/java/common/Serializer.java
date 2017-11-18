package common;

import java.io.*;

public class Serializer {

    public static <T extends Serializable> byte[] serialize(T obj) throws Exception{
        try(ByteArrayOutputStream b = new ByteArrayOutputStream()){
            try(ObjectOutputStream o = new ObjectOutputStream(b)){
                o.writeObject(obj);
            }
            return b.toByteArray();
        }
    }

    public static <T extends Serializable> T deserialize(byte[] data) throws Exception {
        try(ByteArrayInputStream b = new ByteArrayInputStream(data)){
            try(ObjectInputStream o = new ObjectInputStream(b)){
                Object deserialized = o.readObject();
                return (T)deserialized;
            }
        }
    }
}
