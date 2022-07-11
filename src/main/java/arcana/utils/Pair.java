package arcana.utils;

import java.util.Objects;

public final class Pair<A, B>{
	public A a;
	public B b;
	
	public Pair(A first, B second){
		this.a = first;
		this.b = second;
	}
	
	public static <A, B> Pair<A, B> of(A first, B second){
		return new Pair<>(first, second);
	}

	public Pair<B, A> flip(){
		return of(b, a);
	}
	
	public boolean contains(Object obj){
		return a == obj || b == obj;
	}
	
	public boolean equals(Object o){
		if(this == o)
			return true;
		if(!(o instanceof Pair))
			return false;
		Pair<?, ?> pair = (Pair<?, ?>)o;
		return Objects.equals(a, pair.a) && Objects.equals(b, pair.b);
	}
	
	public int hashCode(){
		return Objects.hash(a, b);
	}
}