#include <iostream>
#include "graph.h"
using namespace std;



int compareTo(const Edge* e1 , const Edge* e2){
	//compare each vertex
	int v1 , v2, result; // 0 means same , 1 means e1 > e2 , -1 means e2 > e1

	//test for vertex 1
	if ((e1->v1) > (e2->v1))	v1 = 1;
	else if ((e1->v1) == (e2->v1)) 	v1 = 0 ;
	else 	v1 = -1 ;

	//test for vertex 2
	if ((e1->v2) > (e2->v2))	v2 = 1;
	else if ((e1->v2) == (e2->v2)) 	v2 = 0 ;
	else 	v2 = -1 ;

	//decide the result based on v1 v2
	if (v1==0 && v2==0) 	result = 0;
	else if (v1 == 0)	result = v2 ;
	else if (v1 != 0) 	result = v1;

	return result;
}

 Edge* append(struct Edge* root, int origin, int destination, int weight){
	 struct Edge* result = root;
	 struct Edge* newEdge = new Edge(NULL,origin , destination , weight ); //new edge to be added
	 struct Edge* currentEdge = root ;
	 if (root == NULL)
		 result = newEdge;
	 else {
		 while (currentEdge->nxt != NULL){
			 currentEdge = currentEdge->nxt ;
		 }
		 currentEdge->nxt = newEdge;
	 }
	 return result;
 }

/**
 * print the linked list visually
 **/
void print(Edge* root){
	if (root == NULL)	cout << "END" << endl;
	else {
		cout << "<" << (root->v1) << " |"<< root->weight << "| " << (root->v2) << ">" << "\t" ;
		print(root->nxt);
	}
}
void printV2(Edge* root){
	if (root == NULL) {}
	else {
		cout << " " << (root->v2) << ":" << root->weight <<", ";
		printV2(root->nxt);
	}
}

Edge** orgList(struct Edge *root , const int numberVertices , Edge **arr){
	if (root == NULL) {/*do nothing to the arr and return */
		cout << "finish organizing" << endl;
	}
	else {
		arr[root->v1] = append (arr[root->v1] , root->v1 , root->v2 , root->weight);
//		cout <<"appended";
		arr[root->v2] = append (arr[root->v2], root->v2 , root->v1 , root->weight);
//		cout << "appended " ;
		print(root);
		cout << " to idx: " << root->v1 << "and idx: " << root->v2 <<endl;
		orgList(root->nxt,numberVertices , arr);
	}
	return arr;
}

Edge** organizeList(Edge* root, const int numberVertices){
	Edge **arr;
	arr = new Edge* [numberVertices+1];
//	**arr = NULL;
	return orgList(root, numberVertices , arr);

}
void printOrganized(Edge** adjList,int numberVertices){
	for (int i =0 ; i < numberVertices ; i ++){
		cout << "v" << i << "-> " ;
		printV2(adjList[i]);
		cout << endl;
	}
}

Edge* deleteFirst(Edge* root){
	if (root == NULL) return NULL;
	else {
		Edge* result = root->nxt;
		delete root ;
		return result ;
	}
}

void deleteList (Edge* root){
	if (root == NULL){
		//do nothing
	}
	else {
		deleteList (root->nxt);
		delete root;
	}
}
//
//int main() {
//
//	struct Edge *e1 = new Edge(NULL, 1,2,-1);
//	struct Edge *e2 = new Edge(e1, 1,3,-1);
//	struct Edge *e3 = new Edge(e2, 1,2,-1);
//	struct Edge *e4 = NULL ;
////		print(e4);
//	/**
//	 * Added the -std=c++0x compiler argument under Project Properties
//	 * -> C/C++ Build -> Settings -> GCC C++ Compiler ->
//	 * Miscellaneous. It works now!
//	 */
//	e4 = append(NULL,1,5,3); //TODO will not append to null
//		print(e4);
//	//	cout << e3->v1 <<endl;
//	//	cout << e3->v2 <<endl;
//	//	cout << e3->weight << endl;
//	//	cout << e3->nxt << endl;
//		print(e3);
//	//	print(deleteList(e3));
//	//	cout << compareTo(e1,e2) << endl;
//	Edge ** array ;
//	array = new Edge* [6];
//	//	print(array[1]);
//	//	print(array[0]);
//	array = orgList(e3,5,array);
//	cout << "========="<<endl;
//	printOrganized(array,5);
//	  cout << "Adding all elements" << endl;
//	  Edge* e = append(NULL, 1,4,5);
//	return 0;
//}
