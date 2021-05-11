package xyz.llamachannel.valinsp;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import xyz.llamachannel.valinsp.value.*;


public class LinearLispMachineV1Impl<I> implements LinearLispMachineV1<I> {

  private Map<I, LLMValue> registers;

  private LLMNull theNull = new LLMNull();

  public LinearLispMachineV1Impl() {
    this.registers = new HashMap<>();
  }

  private void validateRegisterPair(I r1, I r2, String functionName) {
    if (!this.registers.containsKey(r1)) {
      throw new RuntimeException(MessageFormat.format("{}: Cannot find first register.",
        functionName));
    }
    if (!this.registers.containsKey(r2)) {
      throw new RuntimeException(MessageFormat.format("{}: Cannot find second register.",
        functionName));
    }
  }

  @Override
  public void swap(I reg1, I reg2) {
    validateRegisterPair(reg1, reg2, "swap");
    LLMValue temp = this.registers.get(reg1);
    this.registers.put(reg1, this.registers.get(reg2));
    this.registers.put(reg2, temp);
  }

  @Override
  public void swapFirst(I reg1, I reg2) {
    validateRegisterPair(reg1, reg2, "swapFirst");
    if (reg1 == reg2) {
      throw new RuntimeException("swapFirst: Registers must be distinct.");
    }
    if (this.registers.get(reg2) instanceof LLMPair pair) {
      LLMValue temp = this.registers.get(reg1);
      this.registers.put(reg1, pair.val2);
      pair.val2 = temp;
    } else {
      throw new IllegalArgumentException("swapFirst: Second register must be non-atomic.");
    }
  }

  @Override
  public void swapSecond(I reg1, I reg2) {
    validateRegisterPair(reg1, reg2, "swapSecond");
    if (reg1 == reg2) {
      throw new RuntimeException("swapSecond: Registers must be distinct.");
    }
    if (this.registers.get(reg2) instanceof LLMPair pair) {
      LLMValue temp = this.registers.get(reg1);
      this.registers.put(reg1, pair.val1);
      pair.val1 = temp;
    } else {
      throw new IllegalArgumentException("swapSecond: Second register must be non-atomic.");
    }
  }

  @Override
  public boolean isNull(I reg) {
    if (!this.registers.containsKey(reg)) {
      throw new RuntimeException("isNull: Cannot find register.");
    }
    return this.registers.get(reg) instanceof LLMNull;
  }

  @Override
  public boolean isAtom(I reg) {
    if (!this.registers.containsKey(reg)) {
      throw new RuntimeException("isAtom: Cannot find register.");
    }
    return !(this.registers.get(reg) instanceof LLMPair);
  }

  @Override
  public boolean eq(I reg1, I reg2) {
    validateRegisterPair(reg1, reg2, "eq");
    if (!isAtom(reg1) || !isAtom(reg2)) {
      throw new RuntimeException("eq: Only atomic-valued registers can be compared.");
    }
    // both are null, or both are same symbol
    return (reg1 instanceof LLMNull && reg2 instanceof LLMNull)
      || ((reg1 instanceof LLMSymbol sym1) && (reg2 instanceof LLMSymbol sym2) && sym1.equals(sym2));
  }

  /**
   * r1 := 'sym
   */
  @Override
  public void assignSymbol(I reg, LLMSymbol symbol) {
    if (!this.registers.containsKey(reg)) {
      throw new RuntimeException("assignSymbol: Cannot find register.");
    }
    if (symbol == null) {
      throw new NullPointerException("assignSymbol: symbol cannot be null.");
    }
    if (isAtom(reg)) {
      throw new RuntimeException("assignSymbol: Can only assign to atomic-valued registers.");
    }
    this.registers.put(reg, symbol);
  }

  /**
   * r1 := NIL
   */
  @Override
  public void assignNull(I reg) {
    if (!this.registers.containsKey(reg)) {
      throw new RuntimeException("assignNull: Cannot find register.");
    }
    if (!isAtom(reg)) {
      throw new RuntimeException("assignNull: Can only assign to atomic-valued registers.");
    }
    this.registers.put(reg, theNull);
  }

  /**
   * r2 := r1
   */
  @Override
  public void assignRegister(I reg1, I reg2){
    validateRegisterPair(reg1, reg2, "assignRegister");
    if (!isAtom(reg1) || !isAtom(reg2)) {
      throw new RuntimeException("assignRegister: Only atomic-valued registers can participate in assignment.");
    }
    this.registers.put(reg1, this.registers.get(reg2));
  }

  // TODO: Baker shows how to implement cons in terms of the swap primitives
  // and a free list
  @Override
  public void cons(I reg1, I reg2) {
    validateRegisterPair(reg1, reg2, "cons");
    var newPair = new LLMPair(this.registers.get(reg1), this.registers.get(reg2));
    this.registers.put(reg1, theNull);
    this.registers.put(reg2, newPair);
  }

  // TODO
  @Override
  public void pop(I reg1, I reg2) {
    validateRegisterPair(reg1, reg2, "pop");
    if (!isNull(reg1) || isAtom(reg2)) {
      throw new RuntimeException("pop: First register must be null, the second register must be non-atomic.");
    }
    LLMPair pair = (LLMPair) this.registers.get(reg2);
    this.registers.put(reg1, pair.val1);
    this.registers.put(reg2, pair.val2);
  }

}
