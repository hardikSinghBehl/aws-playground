package com.behl.chubby.utility;

public class FileSizeConverter {

	public static MB getMb() {
		return new MB();
	}

	public static class MB {
		public long inBytes(final Integer sizeInMb) {
			return (long) sizeInMb * 1024 * 1024;
		}
	}

}
