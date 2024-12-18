package com.travel.service.impl;


import com.travel.dto.salida.CaracteristicaSalidaDto;
import com.travel.entity.Caracteristica;
import com.travel.entity.UserEntity;
import com.travel.exception.NotFoundException;
import com.travel.repository.CaracteristicaRepository;
import com.travel.repository.UserRepository;
import com.travel.service.CaracteristicaService;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CaracteristicaServiceImpl implements CaracteristicaService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final CaracteristicaRepository caracteristicaRepository;

    @Autowired
    public CaracteristicaServiceImpl(UserRepository userRepository, ModelMapper modelMapper, CaracteristicaRepository caracteristicaRepository) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.caracteristicaRepository = caracteristicaRepository;
    }

    @Override
    public List<CaracteristicaSalidaDto> listarTodas() {
        return caracteristicaRepository.findAll().stream()
                .map(caracteristica -> modelMapper.map(caracteristica, CaracteristicaSalidaDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public CaracteristicaSalidaDto obtenerCaracteristicaDtoPorId(Long id) {
        Caracteristica caracteristica = obtenerPorId(id);
        return modelMapper.map(caracteristica, CaracteristicaSalidaDto.class);
    }

    @Override
    public Caracteristica obtenerPorId(Long id) {
        return caracteristicaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Característica no encontrada"));
    }

    @Override
    public CaracteristicaSalidaDto  crear(String currentUserName, Caracteristica caracteristica) {
        UserEntity usuario = userRepository.findByUsername(currentUserName)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));
        caracteristica.setUsuario(usuario);
        Caracteristica nuevaCaracteristica = caracteristicaRepository.save(caracteristica);
        return modelMapper.map(nuevaCaracteristica, CaracteristicaSalidaDto.class);
    }

    @Override
    public CaracteristicaSalidaDto actualizar(Long id, Caracteristica caracteristicaActualizada) {
        Caracteristica caracteristica = obtenerPorId(id);

        caracteristica.setName(caracteristicaActualizada.getName());
        caracteristica.setIcon(caracteristicaActualizada.getIcon());


        Caracteristica actualizada = caracteristicaRepository.save(caracteristica);
        return modelMapper.map(actualizada, CaracteristicaSalidaDto.class);
    }

    @Override
    public void eliminar(Long id) {
        caracteristicaRepository.deleteById(id);
    }

}
