package org.traud.math;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Iterator;

/**
 * Created by traud on 9/11/2016.
 */
public class Permutation implements Iterator<int[]> {

	private final int n;
	private int[] a;

	public Permutation(int n) {
		this.n = n;
	}

	private void init(int n) {
		this.a = new int[n];
		for (int i = 0; i < a.length; ++i)
			a[i] = a[i] = i;
	}

	public BigInteger getTotalPermutationCount() {
		BigInteger f = BigInteger.ONE;
		for (int i = 1; i <= n; ++i) {
			f = f.multiply(BigInteger.valueOf(i));
		}
		return f;
	}

	public int getN() {
		return n;
	}

//	https://en.wikipedia.org/wiki/Permutation#Generation_in_lexicographic_order

//	The following algorithm generates the next permutation lexicographically after a given permutation. It changes the given permutation in-place.
//
//	Find the largest index k such that a[k] < a[k + 1]. If no such index exists, the permutation is the last permutation.
//	Find the largest index l greater than k such that a[k] < a[l].
//	Swap the value of a[k] with that of a[l].
//	Reverse the sequence from a[k + 1] up to and including the final element a[n].

	@Override
	public boolean hasNext() {
		if (a == null) {
			init(n);
			return true;
		}

		int k = -1;
		for (k = a.length-2; k >= 0; --k) {
			if (a[k] < a[k+1]) {
				break;
			}
		}
		if (k == -1) {
			this.a = null;
			return false;
		}

		int l = l = a.length-1;
		while (l > k) {
			if (a[k] < a[l])
				break;
			l--;
		}

		// Swap the value of a[k] with that of a[l].
		int m = a[k];
		a[k] = a[l];
		a[l] = m;

		reverse(a, k+1, a.length);

		return true;
	}

	private void reverse(int[] na, int from, int to) {
		int i = from;
		int j = to-1;
		while (i < j) {
			int m = na[i];
			na[i] = na[j];
			na[j] = m;


			++i;
			--j;
		}
	}

	@Override
	public int[] next() {
		int[] r = a;
		a = a;
		return r;
	}

	public static void main(String[] args) {
		Permutation p = new Permutation(6);
		System.out.format("Permutation of %d elements: %s total permutations\n", p.getN(), p.getTotalPermutationCount());

		int i = 0;
		while (p.hasNext()) {
			System.out.format("%,d: %s\n", ++i, Arrays.toString(p.next()));
		}
	}


}
