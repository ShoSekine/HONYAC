package dic_search;

import java.util.Arrays;

public class JaroWinkler {
	private float threshold = 0.7f;
	private int[] matches(String s1, String s2) {
		String max, min;
		if (s1.length() > s2.length()) {
			max = s1;
			min = s2;
		} else {
			max = s2;
			min = s1;
		}
		int range = Math.max(max.length() / 2 - 1, 0);
		int[] matchIndexes = new int[min.length()];
		Arrays.fill(matchIndexes, -1);
		boolean[] matchFlags = new boolean[max.length()];
		int matches = 0;
		for (int mi = 0; mi < min.length(); mi++) {
			char c1 = min.charAt(mi);
			for (int xi = Math.max(mi - range, 0), xn = Math.min(
					mi + range + 1, max.length()); xi < xn; xi++) {
				if (!matchFlags[xi] && c1 == max.charAt(xi)) {
					matchIndexes[mi] = xi;
					matchFlags[xi] = true;
					matches++;
					break;
				}
			}
		}
		char[] ms1 = new char[matches];
		char[] ms2 = new char[matches];
		for (int i = 0, si = 0; i < min.length(); i++) {
			if (matchIndexes[i] != -1) {
				ms1[si] = min.charAt(i);
				si++;
			}
		}
		for (int i = 0, si = 0; i < max.length(); i++) {
			if (matchFlags[i]) {
				ms2[si] = max.charAt(i);
				si++;
			}
		}
		int transpositions = 0;
		for (int mi = 0; mi < ms1.length; mi++) {
			if (ms1[mi] != ms2[mi]) {
				transpositions++;
			}
		}
		int prefix = 0;
		for (int mi = 0; mi < min.length(); mi++) {
			if (s1.charAt(mi) == s2.charAt(mi)) {
				prefix++;
			} else {
				break;
			}
		}
		return new int[] { matches, transpositions / 2, prefix, max.length() };
	}

	public float getDistance(String s1, String s2) {
		int[] mtp = matches(s1, s2);
		float m = mtp[0];
		if (m == 0) {
			return 0f;
		}
		float j = ((m / s1.length() + m / s2.length() + (m - mtp[1]) / m)) / 3;
		float jw = j < getThreshold() ? j : j + Math.min(0.1f, 1f / mtp[3])
				* mtp[2] * (1 - j);
		return jw;
	}

	/**
	 * Sets the threshold used to determine when Winkler bonus should be used.
	 * Set to a negative value to get the Jaro distance.
	 * 
	 * @param threshold
	 *            the new value of the threshold
	 */
	public void setThreshold(float threshold) {
		this.threshold = threshold;
	}

	/**
	 * Returns the current value of the threshold used for adding the Winkler
	 * bonus. The default value is 0.7.
	 * 
	 * @return the current value of the threshold
	 */
	public float getThreshold() {
		return threshold;
	}
}