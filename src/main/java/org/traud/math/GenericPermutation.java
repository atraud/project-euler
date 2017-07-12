package org.traud.math;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Iterator;

/**
 * Created by traud on 9/11/2016.
 */
public class GenericPermutation<T> implements Iterator<T[]> {

	private final Permutation p;
	private final T[] s;
	private final T[] t;

	public GenericPermutation(T[] s) {
		this.p = new Permutation(s.length);
		this.s = s;
		this.t = (T[])Array.newInstance(s[0].getClass(), s.length);
	}


	@Override
	public boolean hasNext() {
		return p.hasNext();
	}

	@Override
	public T[] next() {
		int[] a = p.next();
		for (int i = 0; i < a.length; ++i) {
			t[i] = s[ a[i] ];
		}
		return t;
	}

	@Override
	public void remove() {
		throw new IllegalStateException("niy");
	}

	public static void main(String[] args) {
		GenericPermutation<Character> p = new GenericPermutation(
			new Character[] { Character.valueOf('a'), Character.valueOf('b'), Character.valueOf('c')}
		);
		int i = 0;
		while (p.hasNext()) {
			System.out.format("%,d: %s\n", ++i, Arrays.toString(p.next()));
		}

		// 1! =    1
		// 2! =    2
		// 3! =    6
		// 4! =   24
		// 5! =  120
		// 6! =  720
		// 7! = 5040
	}


}
