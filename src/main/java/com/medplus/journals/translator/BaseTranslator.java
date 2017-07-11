package com.medplus.journals.translator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class BaseTranslator<F, T> {

  public final List<T> translateTo(Collection<? extends F> froms) {
    List<T> tos = new ArrayList<>();
    if (froms != null) {
      for (F from : froms) {
        tos.add(translateTo(from));
      }
    }
    return tos;
  }

  public final List<T> translateTo(F[] froms) {
    List<T> tos = new ArrayList<>();
    if (froms != null) {
      for (F from : froms) {
        tos.add(translateTo(from));
      }
    }
    return tos;
  }

  public abstract T translateTo(F from);

  public final List<F> translateFrom(Collection<? extends T> tos) {
    List<F> froms = new ArrayList<>();
    if (tos != null) {
      for (T to : tos) {
        froms.add(translateFrom(to));
      }
    }
    return froms;
  }

  public final List<F> translateFrom(T[] tos) {
    List<F> froms = new ArrayList<>();
    if (tos != null) {
      for (T to : tos) {
        froms.add(translateFrom(to));
      }
    }
    return froms;
  }

  public F translateFrom(T to) {
    throw new IllegalStateException("The method translateFrom must be overridden.");
  }
}

