package edu.ufg.aulas.util;

import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.util.HexFormat;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public final class Claves {
    private static final String FORMATO = "pbkdf2_sha256";
    private static final int ITERACIONES = 600_000;
    private static final int BITS = 256;

    private Claves() {
    }

    public static boolean coincide(char[] clave, String registro) {
        String[] partes = registro.split("\\$", -1);
        if (partes.length != 4 || !FORMATO.equals(partes[0])
                || !Integer.toString(ITERACIONES).equals(partes[1])) {
            return false;
        }

        try {
            HexFormat hex = HexFormat.of();
            byte[] sal = hex.parseHex(partes[2]);
            byte[] esperado = hex.parseHex(partes[3]);
            if (sal.length != 16 || esperado.length != BITS / Byte.SIZE) {
                return false;
            }

            PBEKeySpec spec = new PBEKeySpec(clave, sal, ITERACIONES, BITS);
            try {
                byte[] calculado = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
                        .generateSecret(spec).getEncoded();
                return MessageDigest.isEqual(esperado, calculado);
            } finally {
                spec.clearPassword();
            }
        } catch (IllegalArgumentException e) {
            return false;
        } catch (GeneralSecurityException e) {
            throw new IllegalStateException("No se pudo verificar el hash de la clave.", e);
        }
    }
}
