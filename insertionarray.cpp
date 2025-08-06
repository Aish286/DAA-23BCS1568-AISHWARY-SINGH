#include <iostream>
using namespace std;
int main() {
    int arr[100];
    int n, index, element = 20;
    cin >> n;
    for (int i = 0; i < n; i++) {
        cin >> arr[i];
    }
    cin >> index;
    for (int i = n; i > index; i--) {
        arr[i] = arr[i - 1];
    }
    arr[index] = element;
    n++;
    for (int i = 0; i < n; i++) {
        cout << arr[i] << " ";
    }
    return 0;
}
