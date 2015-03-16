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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import edu.asu.emit.algorithm.graph.abstraction.BaseGraph;
import edu.asu.emit.algorithm.graph.abstraction.BaseVertex;
import edu.asu.emit.algorithm.utils.Pair;


/**
 * The class defines a directed graph.
 * 
 * @author yqi
 */
public class Graph implements BaseGraph {
	
	public static final double DISCONNECTED = Double.MAX_VALUE;
	
	// index of fan-outs of one vertex
	protected Map<Integer, Set<BaseVertex>> fanoutVerticesIndex =
		new HashMap<Integer, Set<BaseVertex>>();
	
	// index for fan-ins of one vertex
	protected Map<Integer, Set<BaseVertex>> faninVerticesIndex =
		new HashMap<Integer, Set<BaseVertex>>();
	
	// index for edge weights in the graph
	protected Map<Pair<Integer, Integer>, Double> vertexPairWeightIndex = 
		new HashMap<Pair<Integer, Integer>, Double>();
	
	// index for vertices in the graph
	protected Map<Integer, BaseVertex> idVertexIndex = 
		new HashMap<Integer, BaseVertex>();
	
	// list of vertices in the graph 
	protected List<BaseVertex> vertexList = new Vector<BaseVertex>();
	
	// the number of vertices in the graph
	protected int vertexNum = 0;
	
	// the number of arcs in the graph
	protected int edgeNum = 0;
	
	/**
	 * Constructor 1 
	 * @param dataFileName
	 */
	public Graph(final String dataFileName) {
		importFromFile(dataFileName);
	}
	
	/**
	 * Constructor 2
	 * 
	 * @param graph
	 */
	public Graph(final Graph graph) {
		vertexNum = graph.vertexNum;
		edgeNum = graph.edgeNum;
		vertexList.addAll(graph.vertexList);
		idVertexIndex.putAll(graph.idVertexIndex);
		faninVerticesIndex.putAll(graph.faninVerticesIndex);
		fanoutVerticesIndex.putAll(graph.fanoutVerticesIndex);
		vertexPairWeightIndex.putAll(graph.vertexPairWeightIndex);
	}
	
	/**
	 * Default constructor 
	 */
	public Graph() { }
	
	/**
	 * Clear members of the graph.
	 */
	public void clear() {
		Vertex.reset();
		vertexNum = 0;
		edgeNum = 0; 
		vertexList.clear();
		idVertexIndex.clear();
		faninVerticesIndex.clear();
		fanoutVerticesIndex.clear();
		vertexPairWeightIndex.clear();
	}
	
	/**
	 * There is a requirement for the input graph. 
	 * The ids of vertices must be consecutive. 
	 *  
	 * @param dataFileName
	 */
	public void importFromFile(final String dataFileName) {
		
		// 0. Clear the variables 
		clear();
		
		try	{
			// 1. read the file and put the content in the buffer
			FileReader input = new FileReader(dataFileName);
			BufferedReader bufRead = new BufferedReader(input);

			boolean isFirstLine = true;
			String line; 	// String that holds current file line
			
			// 2. Read first line
			line = bufRead.readLine();
			while (line != null) {
				// 2.1 skip the empty line
				if (line.trim().equals("")) {
					line = bufRead.readLine();
					continue;
				}
				
				// 2.2 generate nodes and edges for the graph
				if (isFirstLine) {
					//2.2.1 obtain the number of nodes in the graph 
					isFirstLine = false;
					vertexNum = Integer.parseInt(line.trim());
					for (int i=0; i<vertexNum; ++i) {
						BaseVertex vertex = new Vertex();
						vertexList.add(vertex);
						idVertexIndex.put(vertex.getId(), vertex);
					}
				} else {
					//2.2.2 find a new edge and put it in the graph  
					String[] strList = line.trim().split("\\s");
					
					int startVertexId = Integer.parseInt(strList[0]);
					int endVertexId = Integer.parseInt(strList[1]);
					double weight = Double.parseDouble(strList[2]);
					addEdge(startVertexId, endVertexId, weight);
				}
				//
				line = bufRead.readLine();
			}
			bufRead.close();

		} catch (IOException e) {
			// If another exception is generated, print a stack trace
			e.printStackTrace();
		}
	}

	/**
	 * Note that this may not be used externally, because some other members in the class
	 * should be updated at the same time. 
	 * 
	 * @param startVertexId
	 * @param endVertexId
	 * @param weight
	 */
	protected void addEdge(int startVertexId, int endVertexId, double weight) {
		// actually, we should make sure all vertices ids must be correct. 
		if (!idVertexIndex.containsKey(startVertexId) || 
			!idVertexIndex.containsKey(endVertexId) || 
			startVertexId == endVertexId) {
			throw new IllegalArgumentException("The edge from " + startVertexId +
					" to " + endVertexId + " does not exist in the graph.");
		}
		
		// update the adjacent-list of the graph
		Set<BaseVertex> fanoutVertexSet = new HashSet<BaseVertex>();
		if (fanoutVerticesIndex.containsKey(startVertexId)) {
			fanoutVertexSet = fanoutVerticesIndex.get(startVertexId);
		}
		fanoutVertexSet.add(idVertexIndex.get(endVertexId));
		fanoutVerticesIndex.put(startVertexId, fanoutVertexSet);
		//
		Set<BaseVertex> faninVertexSet = new HashSet<BaseVertex>();
		if (faninVerticesIndex.containsKey(endVertexId)) {
			faninVertexSet = faninVerticesIndex.get(endVertexId);
		}
		faninVertexSet.add(idVertexIndex.get(startVertexId));
		faninVerticesIndex.put(endVertexId, faninVertexSet);
		// store the new edge 
		vertexPairWeightIndex.put(
				new Pair<Integer, Integer>(startVertexId, endVertexId), 
				weight);
		++edgeNum;
	}
	
	/**
	 * Store the graph information into a file. 
	 * 
	 * @param fileName
	 */
	public void exportToFile(final String fileName) {
		//1. prepare the text to export
		StringBuffer sb = new StringBuffer();
		sb.append(vertexNum + "\n\n");
		for (Pair<Integer, Integer> curEdgePair : vertexPairWeightIndex.keySet()) {
			int startingPtId = curEdgePair.first();
			int endingPtId = curEdgePair.second();
			double weight = vertexPairWeightIndex.get(curEdgePair);
			sb.append(startingPtId + "	" + endingPtId + "	" + weight + "\n");
		}
		//2. open the file and put the data into the file. 
		Writer output = null;
		try {
			// FileWriter always assumes default encoding is OK!
			output = new BufferedWriter(new FileWriter(new File(fileName)));
			output.write(sb.toString());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// flush and close both "output" and its underlying FileWriter
			try {
				if (output != null) {
					output.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public Set<BaseVertex> getAdjacentVertices(BaseVertex vertex) {
		return fanoutVerticesIndex.containsKey(vertex.getId()) 
				? fanoutVerticesIndex.get(vertex.getId()) 
				: new HashSet<BaseVertex>();
	}

	public Set<BaseVertex> getPrecedentVertices(BaseVertex vertex) {
		return faninVerticesIndex.containsKey(vertex.getId()) 
				? faninVerticesIndex.get(vertex.getId()) 
				: new HashSet<BaseVertex>();
	}
	
	public double getEdgeWeight(BaseVertex source, BaseVertex sink)	{
		return vertexPairWeightIndex.containsKey(
					new Pair<Integer, Integer>(source.getId(), sink.getId()))? 
							vertexPairWeightIndex.get(
									new Pair<Integer, Integer>(source.getId(), sink.getId())) 
						  : DISCONNECTED;
	}

	/**
	 * Set the number of vertices in the graph
	 * @param num
	 */
	public void setVertexNum(int num) {
		vertexNum = num;
	}
	
	/**
	 * Return the vertex list in the graph.
	 */
	public List<BaseVertex> getVertexList() {
		return vertexList;
	}
	
	/**
	 * Get the vertex with the input id.
	 * 
	 * @param id
	 * @return
	 */
	public BaseVertex getVertex(int id) {
		return idVertexIndex.get(id);
	}
}
