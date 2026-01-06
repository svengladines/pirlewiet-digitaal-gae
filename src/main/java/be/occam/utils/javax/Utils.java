package be.occam.utils.javax;

import java.util.*;

public class Utils {

	public static<T> T getOrElse( T get, T orElse ){
		return get == null ? orElse : get;
	}

	public static<T> T getOrThrow( T get, RuntimeException exception ) {
		if ( get == null ) {
			throw exception;
		}
		return get;
	}

	public static String whenEmpty( String v, String emptyThen ){
		return v == null || v.isEmpty() ? emptyThen : v;
	}

	/**
	 * Factory for creating arrays. The type of the array is automatically inferred.<br/>
	 * <br/>
	 * Examples:<br/><br/>
	 *  <code>
	 *  array(1,2,3) 	== 	new int[]{1,2,3}<br/><br/>
	 *  array("1","2")  == 	new String[]{"1","2"}<br/>
	 *  </code>
	 */
	public static<T> T[] array(T...xs){
		return xs;
	}

	public static boolean isEmpty(Collection<?> list) {
		return list == null || list.isEmpty();
	}

	public static boolean isNotEmpty(Collection<?> list) {
		return !isEmpty(list);
	}

	public static boolean isEmpty( String s ){
		return s == null || "".equals( s ) || "?".equals( s );
	}

	public static boolean isNotEmpty( String s ){
		return ! isEmpty( s );
	}

	public static boolean isBlank(String string) {
		return string == null || string.trim().isEmpty();
	}

	public static boolean isNotBlank(String string) {
		return !isBlank(string);
	}

	/**
	 * Simple trick to simulate automatic type inference.
	 *
	 *  * The following list construction can be simplified:
	 *
	 *  List<Tuple<MyClassX,Tuple<MyClassY,MyClassZ>>> lst = new ArrayList<Tuple<MyClassX,Tuple<MyClassY,MyClassZ>>>();
	 *
	 *  * We don't get payed per character  ...
	 *
	 *  List<Tuple<MyClassX,Tuple<MyClassY,MyClassZ>>> lst = list();
	 *
	 *
	 */
	public static<T> List<T> list(){
		return new ArrayList<T>();
	}

	/**
	 * Simple trick to simulate automatic type inference.
	 *
	 *  * The following list construction can be simplified:
	 *
	 *  List<Tuple<MyClassX,Tuple<MyClassY,MyClassZ>>> list = new ArrayList<Tuple<MyClassX,Tuple<MyClassY,MyClassZ>>>();
	 *
	 *  * We don't get payed per character  ...
	 *
	 *  List<Tuple<MyClassX,Tuple<MyClassY,MyClassZ>>> list = list();
	 *
	 *
	 */
	public static<T> List<T> list(T...list){
		if (list == null) {
			return new ArrayList<T>(0);
		} else {
			List<T> result = listWithSize(list.length);
            Collections.addAll(result, list);
			return result;
		}
	}

	/**
	 * Returns a new ArrayList with the specified size.
	 * <p>
	 * This method exists as a trick to simulate automatic type inference (see {@link #list()}).
	 *
	 * @param <T>
	 *            the type of the returned list
	 * @param size
	 *            the size of the returned list
	 */
	public static<T> List<T> listWithSize(int size){
		return new ArrayList<T>(size);
	}

	public static<K,V> Map<K,V> map(){
		return new HashMap<K,V>();
	}


	public static<K,V> Map<K,V> map(K k, V v){
		HashMap<K, V> hashMap = new HashMap<K,V>();
		hashMap.put(k, v);
		return hashMap;
	}

	public static<T> Set<T> set(){
		return new HashSet<T>();
	}

	public static<T> Set<T> set( T...set){

		HashSet<T> hashSet = new HashSet<T>();

		if (set != null) {
            Collections.addAll(hashSet, set);
		}

		return hashSet;
	}

	public static<T> Set<T> set( Collection<T> set ){

		HashSet<T> hashSet = new HashSet<T>();

		if (set != null) {
			hashSet.addAll( set );
		}

		return hashSet;
	}

	public static<T> String reduceToString( char separator, Collection<T> collection ){

		StringBuilder reducer = new StringBuilder();

		if (collection != null && !collection.isEmpty()) {
			final Iterator<T> iterator = collection.iterator();

			while( iterator.hasNext() ){

				reducer.append( getOrElse( iterator.next(), "" ).toString() );

				while( iterator.hasNext() ){

					reducer.append( separator );

					reducer.append( getOrElse( iterator.next(), "" ).toString() );

				}

			}
		}

		return reducer.toString();
	}

	public static<T> String reduceToString( char seperator, T ... xs ){

		StringBuilder reducer = new StringBuilder();

		if (xs != null) {
			int i = 0;

			while( i < xs.length ){

				reducer.append( getOrElse( xs[i++], "" ) );

				while( i < xs.length ){

					reducer.append( seperator );

					reducer.append( getOrElse( xs[i++], "" ) );

				}

			}
		}

		return reducer.toString();
	}

	public static<T> String concat(T...xs){

		StringBuilder concat = new StringBuilder();

		if( xs != null ){

			for (T x : xs) {

				concat.append( x );

			}

		}
		return concat.toString();

	}


	public static <T> List<Collection<T>> split( Collection<T> collection, int chunkSize  ){
		if (collection == null || collection.isEmpty()) {
			return new ArrayList<Collection<T>>(0);
		}

		Iterator<T> iterator = collection.iterator();

		Collection<T> tmp = list();

		List<Collection<T>> split = list();

		int n = 0;

		while( iterator.hasNext() ){

			tmp.add( iterator.next() );

			if( ++n % chunkSize == 0 || ! iterator.hasNext() ){

				split.add( tmp );

				tmp = list();

			}

		}

		return split;

	}

	public static<T> List<T> tail( List<T> list ){
		if( list == null || list.size() < 2) {
			return new ArrayList<T>(0);
		} else {
			return list.subList(1, list.size() );
		}
	}

	public  static<T> T head( List<T> list ){
		if(list == null || list.isEmpty()) {
			return null;
		} else {
			return list.get(0);
		}
	}
	
	public static String trim( String s ) {
		
		String result
			= "";
		
		if ( s != null ) {
			result = s.trim();
		}
		
		return result;
		
	}

}
