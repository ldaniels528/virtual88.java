package jbasic.common.tokenizer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Represents a Token Enumeration
 */
public class TokenIterator {
  private final List<String> tokens;
  private int position;

  /**
   * Creates a Token Iterator instance using the given collection
   * to iterate over.
   * @param collection the given {@link Collection collection}
   */
  public TokenIterator( Collection<String> collection ) {	  
	  this.tokens   = new ArrayList<String>( collection );
	  this.position = 0;
  }

  /**
   * Tests the existence of the given token in this iteration
   * @param token the given {@link String token}
   * @return true, if the given token exists in this iteration
   */
  public boolean contains( final String token ) {
	  return tokens.contains( token );
  }

  /**
   * Returns <tt>true</tt> if the iteration has more elements. (In other
   * words, returns <tt>true</tt> if <tt>next</tt> would return an element
   * rather than throwing an exception.)
   * @return <tt>true</tt> if the iterator has more elements.
   */
  public boolean hasNext() {
    return position < tokens.size();
  }

  /**
   * Returns the next element in the iteration.  Calling this method
   * repeatedly until the {@link #hasNext()} method returns false will
   * return each element in the underlying collection exactly once.
   * @return the next element in the iteration.
   * @exception NoSuchElementException iteration has no more elements.
   */
  public String next() {
	  // if we're at the end of the stream, throw exception
	  if( !hasNext() ) throw new NoSuchElementException();
	  
	  // get the object
	  final String token = tokens.get( position++ );

	  // return the next element
	  return token;
  }

  /**
   * Finds the next instance of the given token from the given position
   * in the iteration
   * @param token the given token
   * @return the index of the token within the collection or -1 if not found
   */
  public int nextIndexOf( final String token ) {
	  for( int i = position; i < tokens.size(); i++ ) {
		  if( tokens.get( i ).equals( token ) ) return i;
	  }
	  return -1;
  }

  /**
   * Allows the ability to look ahead without removing the next item from
   * this iterator.
   * @return the next element in the iteration.
   */
  public String peekAtNext() {
	  return peekAtNext( 0 );
  }

  /**
   * Allows the ability to look ahead without removing the next item from
   * this iterator.
   * @return the next element in the iteration.
   */
  public String peekAtNext( final int offset ) {
	  final int index = position + offset;
	  return ( index < tokens.size() ) ? tokens.get( index ) : null;
  }

  /**
   * Removes from the underlying collection the last element returned by the iterator
   * @see java.util.Iterator#remove()
   */
  public void remove() {
	  tokens.remove( position );
  }

  /**
   * Un-gets the given value, placing in back on the stack
   * @param value the given value
   */
  public void unGet( final String token ) {
	  tokens.add( 0, token );
  }

  /**
   * Creates a sub-list containing all tokens up to the first instance
   * of the given token.
   * @param token the given limit token
   * @param movePointer indicates whether to move the iteration pointer
   * @return a {@link TokenIterator sub-list} containing all tokens up to the first instance
   * of the given token.
   */
  public TokenIterator upto( final String token, final boolean movePointer ) {	  
	  // determine the limit 
	  final int limit = contains( token ) ? nextIndexOf( token ) : tokens.size();
	  
	  // create a container for the sublist
	  final Collection<String> collection = new ArrayList<String>( limit - position );
	  
	  // gather all tokens from the current position to the limit
	  collection.addAll( tokens.subList( position, limit ) );
	  
	  // move the current position pointer?
	  if( movePointer ) position = limit;
	  
	  // return a new token iterator
	  return new TokenIterator( collection );    
  }

  /* 
   * (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  public String toString() {
	  final StringBuilder sb = new StringBuilder( 100 );
	  int n = 0;
	  for( int p = position; p < tokens.size(); p++ ) {
		  if( n++ > 0 ) sb.append( ' ' );
		  sb.append( tokens.get( p ) );
	  }	  	  
	  return sb.toString();
  }

}
