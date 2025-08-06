#include <iostream>
using namespace std;
int stack[100], top = -1;
int val = 0;
void push(int value) {
    if (top == 99)
        cout << "Stack is Full\n";
    else {
        top++;
        stack[top] = value;
    }
}
void pop() {
    if (top == -1)
        cout << "Stack is Empty\n";
    else
        val = stack[top];
    top--;
    cout << "value deleted:";
    cout << val;
}
void display() {
    for (int i = top; i >= 0; i--)
        cout << stack[i] << " ";
    cout << endl;
}
int main() {
    push(10);
    push(20);
    push(30);
    display();
    pop();
    cout << endl;
    cout << "after deletion:";
    display();
    return 0;
}
