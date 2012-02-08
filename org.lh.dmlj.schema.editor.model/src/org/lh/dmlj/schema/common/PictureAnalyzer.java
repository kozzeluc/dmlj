package org.lh.dmlj.schema.common;

public abstract class PictureAnalyzer {
	
	public static int getBitCount(String picture) {
		if (!picture.startsWith("X")) {				
			throw new RuntimeException("picture not for bit field");
		}		
		return getCharacterCount(picture);		
	}
	
	public static int getCharacterCount(String picture) {
		if (picture == null || picture.equals("")) {
			return 0;
		}
		if (picture.startsWith("S")) {
			if (picture.length() > 1) {							
				return getDigitCount(picture.substring(1));
			} else {
				return 0;
			}
		}
		int i = picture.indexOf('V');
		if (i != -1) {
			if (picture.length() == 1) {
				return 0;
			}
			if (i == 0) {
				return getDigitCount(picture.substring(1));
			} else if (i == (picture.length() - 1)) {
				return getDigitCount(picture.substring(0, 
										 picture.length() - 1));
			} else {
				return getDigitCount(picture.substring(0, i)) + 
					   getDigitCount(picture.substring(i + 1));
			}
		}
		i = picture.indexOf('(');
		if (i != -1) {
			int j = picture.indexOf(')', i);
			if (j == -1) {
				throw new RuntimeException("unbalanced brackets in picture");
			}
			if ((j - i - 1) == 0) {
				throw new RuntimeException("missing repeat count in picture");
			}
			int k = 0;
			try {
				k = i + Integer.parseInt(picture.substring(i + 1, j)) - 1;
			} catch (NumberFormatException e) {
				throw new RuntimeException("invalid repeat count in picture");
			}
			if (j == (picture.length() - 1)) {
				return k;
			} else {
				return k + getCharacterCount(picture.substring(j + 1)); 
			}
		}		
		return picture.length();
	}
	
	public static int getDigitCount(String picture) {
		if (picture.indexOf("A") != -1 || picture.indexOf("X") != -1 ||
			!picture.startsWith("S") && !picture.startsWith("9") &&
			!picture.startsWith("V")) {
			
			throw new RuntimeException("picture not for numeric field: " + 
									   picture);
		}		
		return getCharacterCount(picture);
	}	
	
	public static int getDigitsAfterVirtualCommaCount(String picture) {
		int i = picture.indexOf('V');
		if (i == -1 || i == (picture.length() - 1)) {
			return 0;
		}
		return getDigitCount(picture.substring(i + 1));
	}
}