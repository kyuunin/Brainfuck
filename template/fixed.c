#include <stdio.h>

#define INC ++*ptr;
#define DEC --*ptr;
#define MVR ++ptr;
#define MVL --ptr;
#define SOL while(*ptr){
#define EOL }
#define PTC putchar(*ptr);
#define GTC *ptr=getchar();

#brainfuck PBRAIN
#if PBRAIN
    typedef void (*function)(char*, void*);
    #define SOP(name)\
        void (name)(char*ptr, void* f){\
            function* funcs = (function*) f; 
    #define EOP }
    #define DEF(name) funcs[*ptr]=(name);
    #define RUN funcs[*ptr](ptr,funcs);
    void null(char*ptr, void* f){}

    #brainfuck PROCE

#endif

#brainfuck SIZE
#brainfuck POS

int main(){
    char array[SIZE]={0};
    char *ptr=array+POS;
    #if PBRAIN
        function funcs[256];
        for(int i=0;i<256;i++){
            funcs[i] = null;
        }
    #endif
    #brainfuck CODE
}
