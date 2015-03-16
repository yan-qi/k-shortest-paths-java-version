/*
 *
 * Copyright (c) 2004-2008 Arizona State University.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY ARIZONA STATE UNIVERSITY ``AS IS'' AND
 * ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL ARIZONA STATE UNIVERSITY
 * NOR ITS EMPLOYEES BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 */
package edu.asu.emit.algorithm.graph;

import edu.asu.emit.algorithm.graph.abstraction.BaseVertex;

/**
 * The class defines a vertex in the graph
 * 
 * @author yqi
 */
public class Vertex implements BaseVertex, Comparable<Vertex> {
	
	private static int currentVertexNum = 0; // Uniquely identify each vertex
	private int id = currentVertexNum++;
	private double weight = 0;
	
	public int getId() {
		return id;
	}

	public String toString() {
		return "" + id;
	}

	public double getWeight() {
		return weight;
	}
	
	public void setWeight(double status) {
		weight = status;
	}
	
	public int compareTo(Vertex rVertex) {
		double diff = this.weight - rVertex.weight;
		if (diff > 0) {
			return 1;
		} else if (diff < 0) {
			return -1;
		} else { 
			return 0;
		}
	}
	
	public static void reset() {
		currentVertexNum = 0;
	}
}
