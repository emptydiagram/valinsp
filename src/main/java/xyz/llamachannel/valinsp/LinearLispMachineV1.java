package xyz.llamachannel.valinsp;

import xyz.llamachannel.valinsp.value.LLMSymbol;

public interface LinearLispMachineV1<I> {
  /**
   * Swap reg1 with reg2
   * @param reg1 an arbitrary register
   * @param reg2 an arbitrary register
   */
  void swap(I reg1, I reg2);

  /**
   * Swap reg1 with the first of (non-atomic) reg2.
   * @param reg1 an arbitrary register
   * @param reg2 a register containing a non-atomic value
   */
  void swapFirst(I reg1, I reg2);

  /**
   * Swap reg1 with the second of (non-atomic) reg2.
   * @param reg1 an arbitrary register
   * @param reg2 a register containing a non-atomic value
   */
  void swapSecond(I reg1, I reg2);

  /**
   * Returns whether the given register is null.
   * @param reg a register
   * @return <code>true</code> if the register is null
   *         <code>false</code> otherwise
   */
  boolean isNull(I reg);

  /**
   * Returns whether the given register is an atom (null or a symbol).
   * @param reg a register
   * @return <code>true</code> if the register holds an atomic value
   *         <code>false</code> otherwise
   */
  boolean isAtom(I reg);

  /**
   * Compares the given atomic-valued registers for equality.
   * @param reg1 a register with an atomic value
   * @param reg2 a register with an atomic value
   * @return <code>true</code> if the atoms are equal
   *         <code>false</code> otherwise
   */
  boolean eq(I reg1, I reg2);

  /**
   * Assign a symbol as the new contents of a register. Prior to the assignment, the
   * register must hold an atomic value.
   * @param reg a register holding an atomic value
   * @param symbol a symbol to write to the register
   */
  void assignSymbol(I reg, LLMSymbol symbol);


  void assignNull(I reg);

  /**
   * Assign the contents of reg2 to reg1.
   * @param reg1 a register holding an atomic value
   * @param reg2 a register holding an atomic value
   */
  void assignRegister(I reg1, I reg2);


  default boolean isSymbol(I reg) {
    return isAtom(reg) && !isNull(reg);
  }

}
