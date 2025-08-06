#include <iostream>
using namespace std;
struct Node {
    int data;
    Node* next;
};
Node *front = NULL, *rear = NULL;
void enqueue(int value) {
    Node* newNode = new Node();
    newNode->data = value;
    newNode->next = NULL;
    if (rear == NULL) {
        front = rear = newNode;
    } else {
        rear->next = newNode;
        rear = newNode;
    }
}
void dequeue() {
    if (front == NULL)
        cout << "Queue is Empty\n";
    else {
        Node* temp = front;
        front = front->next;
        delete temp;
        if (front == NULL)
            rear = NULL;
    }
}
void display() {
    Node* temp = front;
    while (temp != NULL) {
        cout << temp->data << " ";
        temp = temp->next;
    }
    cout << endl;
}
int main() {
    enqueue(10);
    enqueue(20);
    enqueue(30);
    display();
    dequeue();
    display();
    return 0;
}
