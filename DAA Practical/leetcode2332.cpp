class Solution {
public:
    int latestTimeCatchBus(vector<int>& buses, vector<int>& passengers, int capacity) {
        sort(buses.begin(), buses.end());
        sort(passengers.begin(), passengers.end());
         int j = 0, n = passengers.size();
        int lastBoarded = -1;
        int used = 0;
     for (int i = 0; i < buses.size(); i++) {
            used = 0;
            while (j < n && passengers[j] <= buses[i] && used < capacity) {
                lastBoarded = passengers[j];
                j++;
                used++;
            }
        }
       int lastBus = buses.back();
        int ans;
        if (used < capacity) ans = lastBus;
        else ans = lastBoarded - 1;
       while (binary_search(passengers.begin(), passengers.end(), ans)) {
            ans--;
        }
     return ans;
    }
};
