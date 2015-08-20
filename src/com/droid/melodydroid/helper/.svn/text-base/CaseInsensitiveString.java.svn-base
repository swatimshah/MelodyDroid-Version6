package com.droid.melodydroid.helper;

public final class CaseInsensitiveString implements Comparable<Object> {
    private String s;

    public CaseInsensitiveString(String s) {
        if (s == null)
            throw new NullPointerException();
        this.s = s;
    }

    public boolean equals(Object o) {
        return o instanceof CaseInsensitiveString &&
            ((CaseInsensitiveString)o).s.equalsIgnoreCase(s);
    }

    private volatile int hashCode = 0; 

    public int hashCode() {
        if (hashCode == 0)
            hashCode = s.toUpperCase().hashCode();

        return hashCode;
    }

    public int compareTo(Object o) {
        CaseInsensitiveString cis = (CaseInsensitiveString)o;
        return String.CASE_INSENSITIVE_ORDER.compare(s, cis.s);
    }

    public String toString() {
        return s;
    }
}