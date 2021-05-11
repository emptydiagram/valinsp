package xyz.llamachannel.valinsp.value;

public final class LLMPair implements LLMValue {
  public LLMValue val1;
  public LLMValue val2;

  public LLMPair(LLMValue val1, LLMValue val2) {
    this.val1 = val1;
    this.val2 = val2;
  }
}