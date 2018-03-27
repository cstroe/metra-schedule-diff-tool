import com.github.cstroe.metraschedule.domain.Station;

import java.util.Iterator;
import java.util.List;

public class StationIterator implements Iterator<Station> {
    private final List<Station> stations;
    private int currentIndex = 0;

    public StationIterator(List<Station> stations) {
        this.stations = stations;
    }

    @Override
    public boolean hasNext() {
        return currentIndex < stations.size();
    }

    @Override
    public Station next() {
        Station currentStation = stations.get(currentIndex);
        currentIndex++;
        return currentStation;
    }

    public void skipToLast() {
        currentIndex = stations.size() - 1;
    }

    public void goBackTo(int stationId) {
        while(currentIndex > 0) {
            if (stations.get(currentIndex).id == stationId) {
                return;
            }
            currentIndex--;
        }
    }
}
