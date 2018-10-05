package be.kdg.processor.config.mappers;

import be.kdg.processor.config.dtos.CameraMessageDTO;
import be.kdg.processor.domain.CameraMessage;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class CameraMessageMapper {
    private final ModelMapper modelMapper;

    public CameraMessage convertToEntity(CameraMessageDTO msgDto) {
        return modelMapper.map(msgDto, CameraMessage.class);
    }
}
