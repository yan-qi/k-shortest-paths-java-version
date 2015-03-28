/*
 *
 * Copyright (c) 2004-2009 Arizona State University.  All rights
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
package edu.asu.emit.qyan.test;


import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import edu.asu.emit.algorithm.graph.Graph;
import edu.asu.emit.algorithm.graph.shortestpaths.DijkstraShortestPathAlg;

/**
 * TODO Need to redo!
 * @author <a href='mailto:Yan.Qi@asu.edu'>Yan Qi</a>
 * @version $Revision: 784 $
 * @latest $Id: ShortestPathAlgTest.java 784 2009-06-19 20:08:40Z qyan $
 */
public class ShortestPathAlgTest {
	private Graph graph = null;
	
	/**
	 * @throws java.lang.Exception
	 */
	@BeforeTest
	public void setUp() throws Exception {
		// Import the graph from a file
		graph = new Graph("data/test_50");
	}

	@Test
	public void testShorstPathAlg()	{
		System.out.println("Testing Dijkstra Algorithm.");
		DijkstraShortestPathAlg alg = new DijkstraShortestPathAlg(graph);
		System.out.println(alg.getShortestPath(graph.getVertex(0), graph.getVertex(38)));
	}
}
