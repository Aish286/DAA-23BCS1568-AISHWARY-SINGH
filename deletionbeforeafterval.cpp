#include <iostream>
using namespace std;

int main() {
    int arr[100], n, val;
    cin >> n;
    for (int i = 0; i < n; i++) cin >> arr[i];
    cin >> val;
    int pos = -1;
    for (int i = 0; i < n; i++)
        if (arr[i] == val) {
            pos = i;
            break;
        }
    if (pos == -1) cout << "Value not found\n";
    else {
        if (pos > 0) {
            for (int i = pos - 1; i < n - 1; i++) arr[i] = arr[i + 1];
            pos--; n--;
        }
        if (pos < n - 1) {
            for (int i = pos + 1; i < n - 1; i++) arr[i] = arr[i + 1];
            n--;
        }
        for (int i = 0; i < n; i++) cout << arr[i] << " ";
    }
    return 0;
}
