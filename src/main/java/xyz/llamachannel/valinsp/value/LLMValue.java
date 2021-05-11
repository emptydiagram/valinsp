package xyz.llamachannel.valinsp.value;

public sealed interface LLMValue permits LLMNull, LLMSymbol, LLMPair {
}