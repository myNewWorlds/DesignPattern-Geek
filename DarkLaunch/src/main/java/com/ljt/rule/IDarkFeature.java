package com.ljt.rule;

public interface IDarkFeature {
    boolean enabled();
    boolean dark(long darkTarget);
    boolean dark(String darkTarget);
}