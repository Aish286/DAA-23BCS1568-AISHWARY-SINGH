#include <iostream>
using namespace std;
int queue[100];
int front = -1, rear = -1;
int val = 0;
void enqueue(int value) {
    if (rear == 99) {
        cout << "Queue is Full\n";
    } else {
        if (front == -1 && rear == -1) {
            front = rear = 0;
        } else {
            rear++;
        }
        queue[rear] = value;
    }
}
void dequeue() {
    if (front == -1 || front > rear) {
        cout << "Queue is Empty\n";
    } else {
        val = queue[front];
        front++;
        cout << "value deleted:";
        cout << val;
    }
}
void display() {
    if (front == -1 || front > rear) {
        cout << "Queue is Empty\n";
        return;
    }
    for (int i = front; i <= rear; i++) {
        cout << queue[i] << " ";
    }
    cout << endl;
}
int main() {
    enqueue(10);
    enqueue(20);
    enqueue(30);
    display();
    dequeue();
    cout << endl;
    cout << "After deleltion:";
    display();
    return 0;
}
