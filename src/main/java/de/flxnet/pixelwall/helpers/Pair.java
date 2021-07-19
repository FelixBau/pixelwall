package de.flxnet.pixelwall.helpers;

import lombok.Setter;

import lombok.Getter;

/**
 * Software by FLXnet
 * More info at FLXnet.de
 * Copyright (c) 2015-2021 by FLXnet
 * @author Felix
 */
public class Pair<F, S> {

	@Getter @Setter
    private F first;
	
	@Getter @Setter
    private S second;

    public Pair(F first, S second) {
        this.first = first;
        this.second = second;
    }
    
    public static <F, S> Pair<F, S> of(F first, S second) {
    	return new Pair<F, S>(first, second);
    }
	
}
