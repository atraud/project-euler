package org.traud.math;

import java.util.Iterator;

/**
 * Created by traud on 9/11/2016.
 */
public class StringPermutation implements Iterator<String> {

	private final Permutation p;
	private final String s;

	public StringPermutation(String s) {
		this.p = new Permutation(s.length());
		this.s = s;
	}


	@Override
	public boolean hasNext() {
		return p.hasNext();
	}

	@Override
	public String next() {
		int[] a = p.next();

		StringBuilder s = new StringBuilder();
		for (int i = 0; i < a.length; ++i) {
			s.append(this.s.charAt(a[i]));
		}
		return s.toString();
	}

	public static void main(String[] args) {
		StringPermutation p = new StringPermutation("MAXI");
		int i = 0;
		while (p.hasNext()) {
			System.out.format("%,d: %s\n", ++i, p.next());
		}
	}


}
