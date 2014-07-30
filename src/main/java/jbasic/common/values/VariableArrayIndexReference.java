package jbasic.common.values;

/**
 * Represents a reference to an index within a variable array
 */
public interface VariableArrayIndexReference extends VariableReference {

  /**
   * @return the index within the array that is being referenced
   */
  public Value getIndex();

}
