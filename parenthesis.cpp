#include <iostream>
using namespace std;
char stack[100];
int top = -1;
void push(char c) {
    stack[++top] = c;
}
void pop() {
    if (top != -1) top--;
}
char peek() {
    return stack[top];
}
bool isBalanced(string expr) {
    for (char c : expr) {
        if (c == '(' || c == '{' || c == '[')
            push(c);
        else if (c == ')' || c == '}' || c == ']') {
            if (top == -1) return false;
            if ((c == ')' && peek() == '(') ||
                (c == '}' && peek() == '{') ||
                (c == ']' && peek() == '['))
                pop();
            else
                return false;
        }
    }
    return top == -1;
}
int main() {
    string expr;
    cout << "Enter expression: ";
    cin >> expr;
    if (isBalanced(expr))
        cout << "Balanced\n";
    else
        cout << "Not Balanced\n";
    return 0;
}
