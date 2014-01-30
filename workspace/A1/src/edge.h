/*
 * edge.h
 *
 *  Created on: Jan 29, 2014
 *      Author: yuechuan
 */

#ifndef EDGE_H_

struct Edge {
	int v1 , v2, weight ;
	struct Edge* nxt ;
	Edge (Edge* next , int d1 , int d2 , int w){
		nxt = next ;
		v1 = d1;
		v2 = d2 ;
		weight = w ;
	}
};

#define EDGE_H_
#endif /* EDGE_H_ */

