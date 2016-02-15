package com.leela.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.binary.Hex;

import com.leela.model.ObjectModel;

public class CommonsUtil {
    public final static String APPLICATION_LINK = "localhost";
    public final static Long VERIFIED_USER = 1L;
    public final static Long NOT_VERIFIED_USER = 2L;
    public final static Short ACTIVE_STATUS = 1;
    public final static Short IN_ACTIVE_STATUS = 0;
    public final static Short GENDER_MALE = 1;
    public final static Short GENDER_FE_MALE = 2;
    public final static Long USER_TYPE_SU = 1L;
    public final static Long USER_TYPE_USER = 2L;
    public final static Long USER_TYPE_ADMIN = 3L;

    public static boolean isStringBlank(final String value) {
        if (value == null || value.trim().equals("")) {
            return true;
        }
        return false;
    }

    public static String encryptionUsingSHA512(final String inputText)
            throws NoSuchAlgorithmException, UnsupportedEncodingException {
        final MessageDigest md = MessageDigest.getInstance("SHA-512");
        final String encryptedPassword = new String(
                Hex.encodeHex(md.digest(inputText.getBytes("UTF-8"))));

        return encryptedPassword;
    }

    public static Map<String, String> convertToMap(
            final List<Object[]> inputListOfArray) {
        Map<String, String> map = null;
        if (inputListOfArray != null && !inputListOfArray.isEmpty()) {
            map = new LinkedHashMap<String, String>();
            for (final Object[] objectArray : inputListOfArray) {
                map.put(objectArray[0].toString(), objectArray[1] != null
                        ? objectArray[1].toString() : "");
            }
        }
        return map;

    }

    public static List<ObjectModel> convertToObjectModel(
            final List<Object[]> inputListOfArray) {
        List<ObjectModel> list = null;
        if (inputListOfArray != null && !inputListOfArray.isEmpty()) {
            list = new LinkedList<ObjectModel>();
            for (final Object[] objectArray : inputListOfArray) {
                final ObjectModel model = new ObjectModel();
                model.setObjectId(Long.valueOf(objectArray[0].toString()));
                model.setObjectName(objectArray[1] != null
                        ? objectArray[1].toString() : "");
                list.add(model);
            }
        }
        return list;
    }

    public static List<ObjectModel> convertToObjectModelWithStatus(
            final List<Object[]> inputListOfArray) {
        List<ObjectModel> list = null;
        if (inputListOfArray != null && !inputListOfArray.isEmpty()) {
            list = new LinkedList<ObjectModel>();
            for (final Object[] objectArray : inputListOfArray) {
                final ObjectModel model = new ObjectModel();
                model.setObjectId(Long.valueOf(objectArray[0].toString()));
                model.setObjectName(objectArray[1] != null
                        ? objectArray[1].toString() : "");
                model.setStatus(objectArray[2] != null
                        ? Short.valueOf(objectArray[2].toString()) : 0);
                list.add(model);
            }
        }
        return list;
    }

    public static List<ObjectModel> convertToObjectModelWithStatusAndSubObjectId(
            final List<Object[]> inputListOfArray) {
        List<ObjectModel> list = null;
        if (inputListOfArray != null && !inputListOfArray.isEmpty()) {
            list = new LinkedList<ObjectModel>();
            for (final Object[] objectArray : inputListOfArray) {
                final ObjectModel model = new ObjectModel();
                model.setObjectId(Long.valueOf(objectArray[0].toString()));
                model.setObjectName(objectArray[1] != null
                        ? objectArray[1].toString() : "");
                model.setStatus(objectArray[2] != null
                        ? Short.valueOf(objectArray[2].toString()) : 0);
                model.setSubObjectId(objectArray[3] != null
                        ? Long.valueOf(objectArray[3].toString()) : -1L);
                list.add(model);
            }
        }
        return list;
    }

}
