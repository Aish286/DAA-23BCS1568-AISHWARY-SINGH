#include <iostream>
using namespace std;
int main() {
    int a[10][10], b[10][10], result[10][10];
    int r1, c1, r2, c2;
    cin >> r1 >> c1;
    cin >> r2 >> c2;
    if (c1 != r2) {
        cout << "Invalid dimensions";
        return 0;
    }
    for (int i = 0; i < r1; i++)
        for (int j = 0; j < c1; j++)
            cin >> a[i][j];
    for (int i = 0; i < r2; i++)
        for (int j = 0; j < c2; j++)
            cin >> b[i][j];
    for (int i = 0; i < r1; i++)
        for (int j = 0; j < c2; j++) {
            result[i][j] = 0;
            for (int k = 0; k < c1; k++)
                result[i][j] += a[i][k] * b[k][j];
        }
    for (int i = 0; i < r1; i++) {
        for (int j = 0; j < c2; j++)
            cout << result[i][j] << " ";
        cout << endl;
    }
    return 0;
}
