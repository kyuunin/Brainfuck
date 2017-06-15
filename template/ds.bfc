#include <stdio.h>
#include <stdlib.h>

#define INC ++(node->c);
#define DEC --(node->c);

#define MVR\
    if(!node->right){\
		node->right = malloc(sizeof (Node));\
		node->right->c=0;\
		node->right->right=NULL;\
		node->right->left=NULL;\
	}\
	node= node->right;

#define MVL\
	if(!node->left){\
		node->left = malloc(sizeof (Node));\
		node->left->c=0;\
		node->left->right=NULL;\
		node->left->left=NULL;\
	}\
	node= node->left;

#define SOL while(node->c){
#define EOL }
#define PTC putchar(node->c);
#define GTC node->c=getchar();

typedef struct Node Node;
struct Node {
    char c;
    Node* right;
    Node* left;
};

#brainfuck PBRAIN
#if PBRAIN
    typedef void (*function)(Node*, void*);
    #define SOP(name)\
        void (name)(Node* node, void* f){\
            function* funcs = (function*) f; 
    #define EOP }
    #define DEF(name) funcs[node->c]=(name);
    #define RUN funcs[node->c](node,funcs);
    void null(Node* node, void* f){}

    #brainfuck PROCE

#endif


int main(){
    Node* node = malloc(sizeof (Node));
    node->left=NULL;
    node->right=NULL;
    node->c=0;
    #if PBRAIN
        function funcs[256];
        for(int i=0;i<256;i++){
            funcs[i] = null;
        }
    #endif
    #brainfuck CODE
}
