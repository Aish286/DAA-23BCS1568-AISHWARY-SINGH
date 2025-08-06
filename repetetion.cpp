#include <iostream>
using namespace std;

int main() {
    int arr[100], n;
    cin >> n;
    for (int i = 0; i < n; i++) cin >> arr[i];

    for (int i = 0; i < n; i++) {
        bool counted = false;
        for (int j = 0; j < i; j++) {
            if (arr[i] == arr[j]) {
                counted = true;
                break;
            }
        }
        if (!counted) {
            int count = 1;
            for (int j = i + 1; j < n; j++) {
                if (arr[i] == arr[j]) count++;
            }
            cout << arr[i] << ": " << count << endl;
        }
    }
 int del;
    cin >> del;

    int newArr[100], k = 0;
    for (int i = 0; i < n; i++) {
        if (arr[i] != del) {
            newArr[k++] = arr[i];
        }
    }

    for (int i = 0; i < k; i++) cout << newArr[i] << " ";
    cout << endl;

    return 0;
}
