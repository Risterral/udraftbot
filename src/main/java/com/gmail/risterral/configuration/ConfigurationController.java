package com.gmail.risterral.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.gmail.risterral.controllers.log.LogController;
import com.gmail.risterral.controllers.log.LogMessageType;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.SecretKey;
import javax.xml.bind.DatatypeConverter;
import java.io.*;
import java.security.NoSuchAlgorithmException;

public class ConfigurationController {
    private static final ConfigurationController instance = new ConfigurationController();
    private ConfigurationDTO configurationDTO;

    private static final String ALGORITHM = "DES";
    private static final String FORMAT = "RAW";
    private static final String ENCODING = "utf-8";
    private static final String FILE_NAME = "data/UDraftConfig.cfg";
    private static final String KEY = "i4niYu2u";
    private static final String SALT = "LT6mfxghgy";
    private Cipher encrypter;
    private Cipher decrypter;

    private ConfigurationController() {
        try {
            encrypter = Cipher.getInstance(ALGORITHM);
            decrypter = Cipher.getInstance(ALGORITHM);

            prepareFile();
            SecretKey key = prepareKey(KEY);
            encrypter.init(Cipher.ENCRYPT_MODE, key);
            decrypter.init(Cipher.DECRYPT_MODE, key);

            getData();
        } catch (Exception e) {
            LogController.log(this.getClass(), e, LogMessageType.ERROR, "Unexpected error while reading config");
        }
    }

    public static ConfigurationController getInstance() {
        return instance;
    }

    public ConfigurationDTO getConfigurationDTO() {
        return configurationDTO;
    }

    public void getData() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(FILE_NAME), ENCODING));
            StringBuilder data = new StringBuilder();

            String currentLine;
            while ((currentLine = reader.readLine()) != null) {
                data.append(currentLine);
            }
            String decryptedData = decrypt(data.toString());
            if (decryptedData.startsWith(SALT)) {
                ObjectMapper objectMapper = new ObjectMapper();
                configurationDTO = objectMapper.readValue(decryptedData.replaceAll(SALT, ""), ConfigurationDTO.class);
            } else {
                configurationDTO = new ConfigurationDTO();
            }
            reader.close();
        } catch (Exception ignored) {
            configurationDTO = new ConfigurationDTO();
        }
    }

    public void saveData() {
        try {
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(FILE_NAME), ENCODING));
            StringBuilder dataToSave = new StringBuilder();
            dataToSave.append(SALT);

            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
            StringWriter stringWriter = new StringWriter();
            objectMapper.writeValue(stringWriter, configurationDTO);
            dataToSave.append(stringWriter);

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
