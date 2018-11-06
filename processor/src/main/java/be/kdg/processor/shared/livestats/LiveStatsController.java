package be.kdg.processor.shared.livestats;

import be.kdg.processor.camera.CameraService;
import be.kdg.processor.fine.FineService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.ModelAttribute;

/**
 * This controller will add a 'data' model to an existing webpage where live statistics can be shown.
 */
@RequiredArgsConstructor
public class LiveStatsController {
    public final FineService fineService;
    public final CameraService cameraService;

    @ModelAttribute("data")
    public LiveStats addLiveStats() {
        LiveStats liveStats = new LiveStats();
        liveStats.setTotalMessages(cameraService.findAllCameraMessages().size());
        liveStats.setTotalFines(fineService.findAll().size());
        liveStats.setAverageFinePrice(fineService.getAverageFinePrice());
        return liveStats;
    }

}
