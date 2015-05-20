package com.gmail.risterral.gui.config;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.SecretKey;
import javax.xml.bind.DatatypeConverter;
import java.io.*;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedHashMap;

public class ConfigurationFile {
    private static final String ALGORITHM = "DES";
    private static final String FORMAT = "RAW";
    private static final String ENCODING = "utf-8";
    private static final String FILE_NAME = "UDraftConfig.cfg";
    private static final String KEY = "i4niYu2u";
    private static final String SALT = "LT6mfxghgy";
    private static final String SEPARATOR = "Jaf68dE1HX";
    private Cipher encrypter;
    private Cipher decrypter;

    public ConfigurationFile() {
        try {
            encrypter = Cipher.getInstance(ALGORITHM);
            decrypter = Cipher.getInstance(ALGORITHM);

            prepareFile();
            SecretKey key = prepareKey(KEY);
            encrypter.init(Cipher.ENCRYPT_MODE, key);
            decrypter.init(Cipher.DECRYPT_MODE, key);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public LinkedHashMap<String, String> getData() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(FILE_NAME), ENCODING));
            StringBuilder data = new StringBuilder();

            String currentLine;
            while ((currentLine = reader.readLine()) != null) {
                data.append(currentLine);
            }
            String decryptedData = decrypt(data.toString());
            if (decryptedData.startsWith(SALT)) {
                LinkedHashMap<String, String> dataMap = new LinkedHashMap<>();
                for (String dataValue : decryptedData.replaceAll(SALT, "").split(SEPARATOR)) {
                    dataMap.put(dataValue.split("=")[0], dataValue.split("=")[1]);
                }
                return dataMap;
            }
            reader.close();
        } catch (Exception ignored) {
        }
        return null;
    }

    public void saveData(LinkedHashMap<String, String> dataMap) {
        try {
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(FILE_NAME), ENCODING));
            StringBuilder dataToSave = new StringBuilder();
            dataToSave.append(SALT);
            String separator = "";
            for (String dataKey : dataMap.keySet()) {
                dataToSave.append(separator).append(dataKey).append("=").append(dataMap.get(dataKey));
                separator = SEPARATOR;
            }
            writer.write(encrypt(dataToSave.toString()));
            writer.close();
        } catch (Exception ignored) {
            ignored.printStackTrace();
        }
    }

    private void prepareFile() throws IOException {
        File file = new File(FILE_NAME);
        if (!file.exists()) file.createNewFile();
    }

    private SecretKey prepareKey(final String encodedKey) throws NoSuchAlgorithmException {
        return new SecretKey() {
            @Override
            public String getAlgorithm() {
                return ALGORITHM;
            }

            @Override
            public String getFormat() {
                return FORMAT;
            }

            @Override
            public byte[] getEncoded() {
                try {
                    return encodedKey.getBytes(ENCODING);
                } catch (UnsupportedEncodingException e) {
                    return null;
                }
            }
        };
    }

    private String encrypt(String str) throws BadPaddingException, IllegalBlockSizeException, UnsupportedEncodingException {
        return DatatypeConverter.printBase64Binary(encrypter.doFinal(str.getBytes(ENCODING)));
    }

    private String decrypt(String str) throws UnsupportedEncodingException, BadPaddingException, IllegalBlockSizeException {
        return new String(decrypter.doFinal(DatatypeConverter.parseBase64Binary(str)), ENCODING);
    }
}
