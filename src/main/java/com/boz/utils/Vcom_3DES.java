package com.boz.utils;


import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.Security;
import java.security.spec.AlgorithmParameterSpec;


public class Vcom_3DES {
	private String keyStr="";

	private int isEncrypt = -1;// 1为加密，0为解密
	// private String keyStr;// 加密/解密密钥，长度为16byte或者24byte。
	private String message; // 要加密/解密信息（解密时需为十六进制显示的字符串）

	public Vcom_3DES(int isEncrypt, String message) {
		this.isEncrypt = isEncrypt;
		this.message = message;
	}

	public Vcom_3DES(String key) {
		this.keyStr = key;
	}
	private static final Logger LOGGER  = LoggerFactory.getLogger(Vcom_3DES.class);
	public String Vcom3DESChiper() {

		Security.addProvider(new BouncyCastleProvider());
		
		SecretKey key = new SecretKeySpec(keyStr.getBytes(), "DESede");

		byte[] text = null;
		byte[] bmessage = null;
		String returnStr = null;
		try {
			Cipher cipher = Cipher.getInstance("DESede/CBC/PKCS5Padding", "BC");
			AlgorithmParameterSpec algorithmparameterspec = new IvParameterSpec(
					"12345678".getBytes());
			if (isEncrypt == 1) {
				bmessage = message.getBytes();
				cipher.init(Cipher.ENCRYPT_MODE, key, algorithmparameterspec);
			} else if (isEncrypt == 0) {
				bmessage = decodeHex(message);
				cipher.init(Cipher.DECRYPT_MODE, key, algorithmparameterspec);
			} else {
				System.out.println("加解密设置错误，请确认输入：1为加密；0为解密");
				return null;
			}
			text = cipher.doFinal(bmessage);
//			
			if (isEncrypt == 1) {
				returnStr = encodeHex(text);
			} else if (isEncrypt == 0) {
				returnStr = new String(text);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return returnStr;
	}

	public int getIsEncrypt() {
		return isEncrypt;
	}

	public void setIsEncrypt(int isEncrypt) {
		this.isEncrypt = isEncrypt;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public static final String encodeHex(byte bytes[]) {
		/*StringBuffer buf = new StringBuffer(bytes.length * 2);
		for (int i = 0; i < bytes.length; i++) {
			if ((bytes[i] & 0xff) < 16)
				buf.append("0");
			buf.append(Long.toString(bytes[i] & 0xff, 16));
		}
		return buf.toString();*/
	 	return new String(Hex.encode(bytes));
	}

	public static final byte[] decodeHex(String hex) {
	/*	char chars[] = hex.toCharArray();
		byte bytes[] = new byte[chars.length / 2];
		int byteCount = 0;
		for (int i = 0; i < chars.length; i += 2) {
			int newByte = 0;
			newByte |= hexCharToByte(chars[i]);
			newByte <<= 4;
			newByte |= hexCharToByte(chars[i + 1]);
			bytes[byteCount] = (byte) newByte;
			byteCount++;
		}*/
		byte bytes[]=Hex.decode(hex);
		return bytes;
	}

	private static final byte hexCharToByte(char ch) {
		switch (ch) {
		case 48: // '0'
			return 0;

		case 49: // '1'
			return 1;

		case 50: // '2'
			return 2;

		case 51: // '3'
			return 3;

		case 52: // '4'
			return 4;

		case 53: // '5'
			return 5;

		case 54: // '6'
			return 6;

		case 55: // '7'
			return 7;

		case 56: // '8'
			return 8;

		case 57: // '9'
			return 9;

		case 97: // 'a'
			return 10;

		case 98: // 'b'
			return 11;

		case 99: // 'c'
			return 12;

		case 100: // 'd'
			return 13;

		case 101: // 'e'
			return 14;

		case 102: // 'f'
			return 15;

		case 58: // ':'
		case 59: // ';'
		case 60: // '<'
		case 61: // '='
		case 62: // '>'
		case 63: // '?'
		case 64: // '@'
		case 65: // 'A'
		case 66: // 'B'
		case 67: // 'C'
		case 68: // 'D'
		case 69: // 'E'
		case 70: // 'F'
		case 71: // 'G'
		case 72: // 'H'
		case 73: // 'I'
		case 74: // 'J'
		case 75: // 'K'
		case 76: // 'L'
		case 77: // 'M'
		case 78: // 'N'
		case 79: // 'O'
		case 80: // 'P'
		case 81: // 'Q'
		case 82: // 'R'
		case 83: // 'S'
		case 84: // 'T'
		case 85: // 'U'
		case 86: // 'V'
		case 87: // 'W'
		case 88: // 'X'
		case 89: // 'Y'
		case 90: // 'Z'
		case 91: // '['
		case 92: // '\\'
		case 93: // ']'
		case 94: // '^'
		case 95: // '_'
		case 96: // '`'
		default:
			return 0;
		}
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String key = "vcomnryyvcomnryyvcomnryy";
//		Vcom_3DES vcom3DES = new Vcom_3DES("PropertyUtil.PLAY_SERVER_DES");
		Vcom_3DES vcom3DES = new Vcom_3DES(key);
		vcom3DES.setIsEncrypt(1);

		String msg = "concernAccount=4101011999990005&role=3&username=41010100010128";
//		msg = "4201010030010013";
		vcom3DES.setMessage(msg);
		String outStr = vcom3DES.Vcom3DESChiper();
		System.out.println(outStr);
		
		
//		vcom3DES.setIsEncrypt(1);
//		vcom3DES.setMessage("admin");
//		outStr = vcom3DES.Vcom3DESChiper();

		
		
		vcom3DES.setIsEncrypt(0);
		vcom3DES.setMessage(outStr);
		String outStr1 = vcom3DES.Vcom3DESChiper();
		System.out.println(outStr1);

		
	}

	public String getKeyStr() {
		return keyStr;
	}

	public void setKeyStr(String keyStr) {
		this.keyStr = keyStr;
	}
}
