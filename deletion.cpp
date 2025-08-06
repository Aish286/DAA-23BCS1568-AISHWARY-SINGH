#include <iostream>
using namespace std;

int main() {
    int arr[100], n, val;
    cin >> n;
    for (int i = 0; i < n; i++) cin >> arr[i];
    cin >> val;
    int i;
    for (i = 0; i < n; i++)
        if (arr[i] == val) break;
    if (i == n) cout << "Value not found\n";
    else {
        for (; i < n - 1; i++) arr[i] = arr[i + 1];
        for (i = 0; i < n - 1; i++) cout << arr[i] << " ";
    }
    return 0;
}
