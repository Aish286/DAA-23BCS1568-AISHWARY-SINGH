#include<iostream>
using namespace std;

struct Node {
    int data;
    Node* left;
    Node* right;

    // Proper constructor with body enclosed in {}
    Node(int val) {
        this->data = val;
        this->left = nullptr;
        this->right = nullptr;
    }
};

Node* buildtree(Node* root) {
    cout << "Enter the data: ";
    int data;
    cin >> data;

    if (data == -1) {
        return nullptr;
    }

    root = new Node(data);

    cout << "Enter data for left of " << data << endl;
    root->left = buildtree(root->left);

    cout << "Enter data for right of " << data << endl;
    root->right = buildtree(root->right);

    return root;
}

void preorder(Node* root) {
    if (root == nullptr) {
        return;
    }
    cout << root->data << " ";
    preorder(root->left);
    preorder(root->right);
}

void postorder(Node* root) {
    if (root == nullptr) {
        return;
    }
    postorder(root->left);
    postorder(root->right);
    cout << root->data << " ";
}

int main() {
    Node* root = nullptr;
    root = buildtree(root);
    
    cout << "Preorder traversal: ";
    preorder(root);
    cout << endl;
    
    cout << "Postorder traversal: ";
    postorder(root);
    cout << endl;

    return 0;
}
