package org.securitybroker.util;

public final class Hex {

	private static final char[] hexChars = {
		'0', '1', '2', '3', '4', '5', '6', '7',
		'8', '9', 'a', 'b',	'c', 'd', 'e', 'f'
	};

	public static String encode(final byte[] b) {
		final char[] buf = new char[b.length << 1];

		for (int i=0, j=0; i < b.length; i++) {
			buf[j++] = hexChars[(b[i] >>> 4) & 0x0f];
			buf[j++] = hexChars[b[i] & 0x0f];
		}

		return new String(buf);
	}

}	// End Hex
