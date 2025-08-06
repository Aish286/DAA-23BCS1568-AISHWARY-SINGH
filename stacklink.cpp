#include <iostream>
using namespace std;
struct Node {
    int data;
    Node* next;
};
Node* top = NULL;
void push(int value) {
    Node* newNode = new Node();
    newNode->data = value;
    newNode->next = top;
    top = newNode;
}
void pop() {
    if (top == NULL)
        cout << "Stack is Empty\n";
    else {
        Node* temp = top;
        top = top->next;
        delete temp;
    }
}
void display() {
    Node* temp = top;
    while (temp != NULL) {
        cout << temp->data << " ";
        temp = temp->next;
    }
    cout << endl;
}
int main() {
    push(5);
    push(15);
    push(25);
    display();
    pop();
    display();
    return 0;
}
