package az.zero.azcypher.cypher;

public interface Cipher {
    String encrypt(String message);

    String decrypt(String message);

}
