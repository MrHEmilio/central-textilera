package mx.software.solutions.centraltextileraonline.apiservices.service.implementation;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

import mx.software.solutions.centraltextileraonline.apiservices.entities.OrderClothEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.BoxCreateRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.BoxUpdateRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.CalculateBoxRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.FilterRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.request.PaginationRequest;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.BoxCalculateAmountResponse;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.BoxCalculateResponse;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.BoxHistoryResponse;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.BoxResponse;
import mx.software.solutions.centraltextileraonline.apiservices.controllers.responses.GetResponse;
import mx.software.solutions.centraltextileraonline.apiservices.entities.BoxEntity;
import mx.software.solutions.centraltextileraonline.apiservices.entities.BoxHistoryEntity;
import mx.software.solutions.centraltextileraonline.apiservices.enumerations.Controller;
import mx.software.solutions.centraltextileraonline.apiservices.enumerations.DataBaseActionType;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.DataBaseException;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.ExistException;
import mx.software.solutions.centraltextileraonline.apiservices.exceptions.NotFoundException;
import mx.software.solutions.centraltextileraonline.apiservices.helpers.AdminHelper;
import mx.software.solutions.centraltextileraonline.apiservices.helpers.BoxHelper;
import mx.software.solutions.centraltextileraonline.apiservices.helpers.OrderHelper;
import mx.software.solutions.centraltextileraonline.apiservices.helpers.PaginationHelper;
import mx.software.solutions.centraltextileraonline.apiservices.repositories.BoxHistoryRepository;
import mx.software.solutions.centraltextileraonline.apiservices.repositories.BoxRepository;
import mx.software.solutions.centraltextileraonline.apiservices.service.BoxService;

@Slf4j
@Service
public class BoxServiceImplementation implements BoxService {

    @Autowired
    private BoxRepository boxRepository;

    @Autowired
    private BoxHistoryRepository boxHistoryRepository;

    @Autowired
    private OrderHelper orderHelper;

    @Autowired
    private BoxHelper boxHelper;

    @Autowired
    private PaginationHelper paginationHelper;

    @Autowired
    private AdminHelper adminHelper;

    @Autowired
    ShippingPqtxServiceImplementation calculateBox;

    private final BigDecimal grosorTela = new BigDecimal("0.05713");

    @Override
    public BoxResponse createBox(final BoxCreateRequest boxCreateRequest, final UUID admin) throws DataBaseException, ExistException {
        BoxServiceImplementation.log.info("Starting created the box {}.", boxCreateRequest.getName());
        this.validateNameNotExist(boxCreateRequest.getName());
        this.validateColorCodeNotExist(boxCreateRequest.getColorCode());
        this.validateWidthHeightDepthNotExist(boxCreateRequest.getWidth(), boxCreateRequest.getHeight(), boxCreateRequest.getDepth());
        try {
            final var boxEntity = new BoxEntity();
            boxEntity.setName(boxCreateRequest.getName());
            boxEntity.setWidth(boxCreateRequest.getWidth());
            boxEntity.setHeight(boxCreateRequest.getHeight());
            boxEntity.setDepth(boxCreateRequest.getDepth());
            boxEntity.setColorCode(boxCreateRequest.getColorCode());
            boxEntity.setAmount(new BigDecimal(1000));
            boxEntity.setActive(true);
            final var newBoxEntity = this.boxRepository.save(boxEntity);
            BoxServiceImplementation.log.info("Finished create the box {}.", boxCreateRequest.getName());

            BoxServiceImplementation.log.info("Starting created the create history {}.", boxCreateRequest.getName());
            this.createBoxHistory(newBoxEntity, admin, DataBaseActionType.CREATE);
            BoxServiceImplementation.log.info("Finished create the create history {}.", boxCreateRequest.getName());

            return this.boxHelper.convertBox(newBoxEntity);
        } catch (final Exception exception) {
            BoxServiceImplementation.log.error("The box {} could not been create.", boxCreateRequest.getName(), exception);
            throw new DataBaseException(Controller.CATALOG_BOX, DataBaseActionType.CREATE, boxCreateRequest.getName());
        }
    }

    @Override
    public BoxResponse updateBox(final BoxUpdateRequest boxUpdateRequest, final UUID admin) throws DataBaseException, NotFoundException, ExistException {
        BoxServiceImplementation.log.info("Starting updated the box with the id {}.", boxUpdateRequest.getId());
        final var boxEntity = this.boxHelper.getBoxEntity(boxUpdateRequest.getId());
        boxEntity.setName(boxUpdateRequest.getName());
        boxEntity.setWidth(boxUpdateRequest.getWidth());
        boxEntity.setHeight(boxUpdateRequest.getHeight());
        boxEntity.setDepth(boxUpdateRequest.getDepth());
        boxEntity.setColorCode(boxUpdateRequest.getColorCode());
        boxEntity.setActive(true);
        try {
            final var newBoxEntity = this.boxRepository.save(boxEntity);
            BoxServiceImplementation.log.info("Finished update the box with the id {}.", boxUpdateRequest.getId());

            BoxServiceImplementation.log.info("Starting created the update history of id {}.", boxUpdateRequest.getId());
            this.createBoxHistory(newBoxEntity, admin, DataBaseActionType.UPDATE);
            BoxServiceImplementation.log.info("Finished create the update history of id {}.", boxUpdateRequest.getId());

            return this.boxHelper.convertBox(newBoxEntity);
        } catch (final Exception exception) {
            BoxServiceImplementation.log.error("The box with the id {} could not been update.", boxUpdateRequest.getId(), exception);
            throw new DataBaseException(Controller.CATALOG_BOX, DataBaseActionType.UPDATE, boxUpdateRequest.getName());
        }
    }

    @Override
    public BoxResponse reactivateBox(final UUID box, final UUID admin) throws DataBaseException, NotFoundException {
        BoxServiceImplementation.log.info("Starting reactivated the box with the id {}.", box);
        final var boxEntity = this.boxHelper.getBoxEntity(box);
        boxEntity.setActive(true);
        try {
            final var newBoxEntity = this.boxRepository.save(boxEntity);
            BoxServiceImplementation.log.info("Finished reactivate the box with the id {}.", box);

            BoxServiceImplementation.log.info("Starting created the reactivate history of id {}.", box);
            this.createBoxHistory(newBoxEntity, admin, DataBaseActionType.REACTIVATE);
            BoxServiceImplementation.log.info("Finished create the reactivate history of id {}.", box);

            return this.boxHelper.convertBox(newBoxEntity);
        } catch (final Exception exception) {
            BoxServiceImplementation.log.error("The box with the id {} could not been reactivate.", box, exception);
            throw new DataBaseException(Controller.CATALOG_BOX, DataBaseActionType.REACTIVATE, box.toString());
        }
    }

    @Override
    public BoxResponse deleteBox(final UUID box, final UUID admin) throws DataBaseException, NotFoundException {
        BoxServiceImplementation.log.info("Starting deleted the box with the id {}.", box);
        final var boxEntity = this.boxHelper.getBoxEntity(box);
        boxEntity.setActive(false);
        try {
            final var newBoxEntity = this.boxRepository.save(boxEntity);
            BoxServiceImplementation.log.info("Finished delete the box with the id {}.", box);

            BoxServiceImplementation.log.info("Starting created the delete history of id {}.", box);
            this.createBoxHistory(newBoxEntity, admin, DataBaseActionType.DELETE);
            BoxServiceImplementation.log.info("Finished create the delete history of id {}.", box);

            return this.boxHelper.convertBox(newBoxEntity);
        } catch (final Exception exception) {
            BoxServiceImplementation.log.error("The box with the id {} could not been delete.", box, exception);
            throw new DataBaseException(Controller.CATALOG_BOX, DataBaseActionType.DELETE, box.toString());
        }
    }

    @Override
    public GetResponse<BoxResponse> getAllBox(final FilterRequest filterRequest, final PaginationRequest paginationRequest) throws DataBaseException, NotFoundException {
        BoxServiceImplementation.log.info("Starting searched of all boxes.");
        Page<BoxEntity> pageBoxEntity = null;
        try {
            final var search = filterRequest.getSearch();
            final var active = filterRequest.getActive();
            String direction = null;
            if (paginationRequest.getTypeSort() != null)
                direction = paginationRequest.getTypeSort().name();
            paginationRequest.setColumnSort(null);
            paginationRequest.setTypeSort(null);
            final var pageable = this.paginationHelper.getPageable(paginationRequest);
            pageBoxEntity = this.boxRepository.findAll(search, active, direction, pageable);
        } catch (final Exception exception) {
            BoxServiceImplementation.log.error("The box could not been read.", exception);
            throw new DataBaseException(Controller.CATALOG_BOX, DataBaseActionType.READ);
        }
        final var listBoxResponse = pageBoxEntity.get().map(this.boxHelper::convertBox).collect(Collectors.toList());
        listBoxResponse.sort(Comparator.comparing(BoxResponse::getActive).reversed());

        final var paginationResponse = this.paginationHelper.getPaginationResponse(pageBoxEntity);
        if (listBoxResponse.isEmpty()) {
            BoxServiceImplementation.log.error("The boxes not found.");
            throw new NotFoundException(Controller.CATALOG_BOX, "all");
        }
        BoxServiceImplementation.log.info("Finished search the box.");
        return new GetResponse<>(listBoxResponse, paginationResponse);
    }

    @Override
    public GetResponse<BoxHistoryResponse> getBoxHistory(final UUID box, final PaginationRequest paginationRequest) throws DataBaseException, NotFoundException {
        BoxServiceImplementation.log.info("Starting searched history of box with the id {}.", box);
        final var boxEntity = this.boxHelper.getBoxEntity(box);
        Page<BoxHistoryEntity> pageBoxHistoryEntity = null;
        try {
            final var pageable = this.paginationHelper.getPageable(paginationRequest);
            pageBoxHistoryEntity = this.boxHistoryRepository.findAllByBoxEntity(boxEntity, pageable);
        } catch (final Exception exception) {
            BoxServiceImplementation.log.error("The history box could not been read.", exception);
            throw new DataBaseException(Controller.CATALOG_BOX, DataBaseActionType.READ);
        }
        final var listBoxHistoryResponse = pageBoxHistoryEntity.get().map(this.boxHelper::convertBoxHistory).collect(Collectors.toList());
        final var paginationResponse = this.paginationHelper.getPaginationResponse(pageBoxHistoryEntity);
        BoxServiceImplementation.log.info("Finished search history of box with the id {}.", box);
        return new GetResponse<>(listBoxHistoryResponse, paginationResponse);
    }

    @Override
    public List<BoxCalculateResponse> calculateBox(final CalculateBoxRequest calculateBoxRequest) throws DataBaseException, NotFoundException {

        BoxServiceImplementation.log.info("Starting calculate the box with the order {}.", calculateBoxRequest.getOrder());

        List<BoxCalculateResponse> listBoxCalculateResponse = new ArrayList<>();

        final List<BoxDto> listBoxesDto = new ArrayList<>();
        final var listBoxEntity = this.boxRepository.findAll();

        if (listBoxEntity.spliterator().getExactSizeIfKnown() == 0) {
            BoxServiceImplementation.log.error("The boxes not found.");
            throw new NotFoundException(Controller.CATALOG_BOX, "all");
        }

        listBoxEntity.forEach(boxEntity -> {

            if (boxEntity.isActive()) {
                final var volumen = boxEntity.getWidth().multiply(boxEntity.getHeight()).multiply(boxEntity.getDepth());
                final var boxDto = new BoxDto();
                boxDto.setBoxEntity(boxEntity);
                boxDto.setVolumen(volumen);
                listBoxesDto.add(boxDto);
            }

        });

        final var listBoxesDtoSorted = listBoxesDto.stream().sorted(Comparator.comparing(BoxDto::getVolumen)).collect(Collectors.toList());

        BoxDto rolloBox = listBoxesDtoSorted.get(0);
        BoxDto miniRolBox = listBoxesDtoSorted.get(1);

        String mt = "metro";
        double volumenCorte = 0.0;
        double pesoTotalCorte = 0.0;

        final var porciento = 100;
        final var minPorciento = 70;

        PackageDto packageDto;

        final var orderEntity = this.orderHelper.getOrderEntity(calculateBoxRequest.getOrder());

        for (OrderClothEntity orderClothEntity : orderEntity.getOrderClothEntities()) {
            final List<BoxCalculateAmountResponse> listBoxCalculateAmountResponse = new ArrayList<>();
            final var clothEntity = orderClothEntity.getClothEntity().getClothMeasureEntity();
            final double diametro = clothEntity.getDimension().doubleValue();
            final double amout = orderClothEntity.getAmount().doubleValue();
            final double avRoll = clothEntity.getAveragePerRoll().doubleValue();
            final double width = clothEntity.getWidth().doubleValue();
            final double grBym2 = clothEntity.getWeight().doubleValue();
            final double grByml = (width / 100) * grBym2; //El peso está en gramos

            final var medidaVenta = clothEntity.getClothEntity().getClothBillingEntity().getUnitLabel();
            double percent;
            double dimResto;

            double tempVol;

            if (clothEntity.getYieldPerKilo() != null) {
                final var rendimiento = clothEntity.getClothEntity().getClothMeasureEntity().getYieldPerKilo();

                if (amout >= avRoll) {
                    double[] resultado = splitIntDouble(amout, avRoll);
                    int cantRolloKg = (int) resultado[0];

                    if (resultado.length > 1) {
                        double corteRoll = resultado[1];

                        percent = (corteRoll * porciento) / avRoll;
                        if (percent >= minPorciento) {
                            dimResto = diameTemp(corteRoll, diametro, avRoll);
                            tempVol = volumenRollo(dimResto, width);
                            packageDto = new PackageDto();
                            packageDto.setVolumenTotal(BigDecimal.valueOf(tempVol));

                            miniRolBox.getBoxEntity().setHeight(BigDecimal.valueOf(dimResto).setScale(3, RoundingMode.HALF_UP));
                            miniRolBox.getBoxEntity().setDepth(BigDecimal.valueOf(dimResto).setScale(3, RoundingMode.HALF_UP));
                            miniRolBox.getBoxEntity().setWidth(BigDecimal.valueOf(width).setScale(3, RoundingMode.HALF_UP));
                            miniRolBox.setVolumen(BigDecimal.valueOf(tempVol));

                            packageDto.setBoxResponse(createBxResp(miniRolBox));

                            listBoxCalculateAmountResponse.add(new BoxCalculateAmountResponse(1, packageDto.getBoxResponse()));
                        }
                        else {
                            volumenCorte += (((corteRoll * rendimiento.doubleValue()) * 100) * grosorTela.doubleValue() * width);
                            pesoTotalCorte += (corteRoll * rendimiento.doubleValue()) * 100;
                        }
                    }

                    packageDto = new PackageDto();
                    packageDto.setVolumenTotal(BigDecimal.valueOf(volumenRollo(diametro, width)));

                    rolloBox.getBoxEntity().setHeight(BigDecimal.valueOf(diametro).setScale(3, RoundingMode.HALF_UP));
                    rolloBox.getBoxEntity().setDepth(BigDecimal.valueOf(diametro).setScale(3, RoundingMode.HALF_UP));
                    rolloBox.getBoxEntity().setWidth(BigDecimal.valueOf(width).setScale(3, RoundingMode.HALF_UP));
                    rolloBox.setVolumen(BigDecimal.valueOf(volumenRollo(diametro, width)));

                    packageDto.setBoxResponse(createBxResp(rolloBox));
                    listBoxCalculateAmountResponse.add(new BoxCalculateAmountResponse(cantRolloKg, packageDto.getBoxResponse()));
                } else {
                    percent = (amout * porciento) / avRoll;
                    if (percent > minPorciento) {
                        dimResto = diameTemp(amout, diametro, avRoll);
                        tempVol = volumenRollo(dimResto, width);
                        packageDto = new PackageDto();
                        packageDto.setVolumenTotal(BigDecimal.valueOf(tempVol));

                        miniRolBox.getBoxEntity().setHeight(BigDecimal.valueOf(dimResto).setScale(3, RoundingMode.HALF_UP));
                        miniRolBox.getBoxEntity().setDepth(BigDecimal.valueOf(dimResto).setScale(3, RoundingMode.HALF_UP));
                        miniRolBox.getBoxEntity().setWidth(BigDecimal.valueOf(width).setScale(3, RoundingMode.HALF_UP));
                        miniRolBox.setVolumen(BigDecimal.valueOf(tempVol));

                        packageDto.setBoxResponse(createBxResp(miniRolBox));

                        listBoxCalculateAmountResponse.add(new BoxCalculateAmountResponse(1, packageDto.getBoxResponse()));
                    } else {
                        volumenCorte += ((amout * rendimiento.doubleValue()) * 100) * grosorTela.doubleValue() * width;
                        pesoTotalCorte += amout;
                    }
                }//END ELSE SI LA CANTIDAD ES MENOR QUE EL TAMAÑO DEL ROLLO EN kg
            } else if (medidaVenta.equalsIgnoreCase(mt)) {
                double pesoMts;
                if (amout >= avRoll) {
                    double[] resultado = splitIntDouble(amout, avRoll);
                    int cantRolloMt = (int) resultado[0];
                    if (resultado.length > 1) {
                        double restoRollo = resultado[1];
                        percent = (restoRollo * porciento) / avRoll;
                        if (percent >= minPorciento) {
                            dimResto = diameTemp(restoRollo, diametro, avRoll);
                            tempVol = volumenRollo(dimResto, width);
                            packageDto = new PackageDto();
                            packageDto.setVolumenTotal(BigDecimal.valueOf(tempVol));

                            miniRolBox.getBoxEntity().setHeight(BigDecimal.valueOf(dimResto).setScale(3, RoundingMode.HALF_UP));
                            miniRolBox.getBoxEntity().setDepth(BigDecimal.valueOf(dimResto).setScale(3, RoundingMode.HALF_UP));
                            miniRolBox.getBoxEntity().setWidth(BigDecimal.valueOf(width).setScale(3, RoundingMode.HALF_UP));
                            miniRolBox.setVolumen(BigDecimal.valueOf(tempVol));

                            packageDto.setBoxResponse(createBxResp(miniRolBox));
                            listBoxCalculateAmountResponse.add(new BoxCalculateAmountResponse(1, packageDto.getBoxResponse()));
                        } else {
                            pesoMts = tempPeso(restoRollo, grByml);
                            volumenCorte += (((restoRollo* 100) * grosorTela.doubleValue()) ) * width;
                            pesoTotalCorte += pesoMts;
                        }
                    }
                    packageDto = new PackageDto();
                    packageDto.setVolumenTotal(BigDecimal.valueOf(volumenRollo(diametro, width)));

                    rolloBox.getBoxEntity().setHeight(BigDecimal.valueOf(diametro).setScale(3, RoundingMode.HALF_UP));
                    rolloBox.getBoxEntity().setDepth(BigDecimal.valueOf(diametro).setScale(3, RoundingMode.HALF_UP));
                    rolloBox.getBoxEntity().setWidth(BigDecimal.valueOf(width).setScale(3, RoundingMode.HALF_UP));
                    rolloBox.setVolumen(BigDecimal.valueOf(volumenRollo(diametro, width)).setScale(3, RoundingMode.HALF_UP));
                    packageDto.setBoxResponse(createBxResp(rolloBox));
                    listBoxCalculateAmountResponse.add(new BoxCalculateAmountResponse(cantRolloMt, packageDto.getBoxResponse()));
                } else {
                    percent = (amout * porciento) / avRoll;
                    if (percent >= minPorciento) {
                        dimResto = diameTemp(amout, diametro, avRoll);
                        tempVol = volumenRollo(dimResto, width);
                        packageDto = new PackageDto();
                        packageDto.setVolumenTotal(BigDecimal.valueOf(tempVol));

                        miniRolBox.getBoxEntity().setHeight(BigDecimal.valueOf(dimResto).setScale(3, RoundingMode.HALF_UP));
                        miniRolBox.getBoxEntity().setDepth(BigDecimal.valueOf(dimResto).setScale(3, RoundingMode.HALF_UP));
                        miniRolBox.getBoxEntity().setWidth(BigDecimal.valueOf(width).setScale(3, RoundingMode.HALF_UP));
                        miniRolBox.setVolumen(BigDecimal.valueOf(tempVol).setScale(3, RoundingMode.HALF_UP));

                        packageDto.setBoxResponse(createBxResp(miniRolBox));

                        listBoxCalculateAmountResponse.add(new BoxCalculateAmountResponse(1, packageDto.getBoxResponse()));
                    } else {
                        pesoMts = tempPeso(amout, grByml);
                        volumenCorte += ((amout * 100) * grosorTela.doubleValue()) * width;
                        pesoTotalCorte += pesoMts;
                    }
                }
            } else {
                System.out.println("MEDIDA EN PIEZAS, PENDIENTE!!!");
            }///END ELSE VENTA EN METRO -> piezas

            final var clothName = orderClothEntity.getClothName();
            final var colorName = orderClothEntity.getColorName();
            final var boxCalculateResponse = new BoxCalculateResponse(clothName, colorName, listBoxCalculateAmountResponse);

            listBoxCalculateResponse.add(boxCalculateResponse);

        }//END FOR PRINCIPAL

        if (volumenCorte > 0) {
            double volMax = listBoxesDtoSorted.get(listBoxesDtoSorted.size() - 1).getVolumen().doubleValue();
            BoxDto boxMax = listBoxesDtoSorted.get(listBoxesDtoSorted.size() - 1);

            if (volumenCorte > volMax) {
                double[] respuesta = splitIntDouble(volumenCorte, volMax);

                int numBoxMax = (int) respuesta[0];

                if (respuesta.length > 1) {
                    double restoVolumen = respuesta[1];

                    for (BoxDto box : listBoxesDtoSorted) {
                        if (box.getBoxEntity().getName().equalsIgnoreCase("Rollo") || box.getBoxEntity().getName().equalsIgnoreCase("Rollo Mini"))
                            continue;
                        double volTem = box.getVolumen().doubleValue();
                        if (restoVolumen <= volTem) {
                            packageDto = new PackageDto();
                            packageDto.setVolumenTotal(BigDecimal.valueOf(volTem));
                            packageDto.setBoxResponse(createBxResp(box));
                            listBoxCalculateResponse.get(listBoxCalculateResponse.size() - 1).getBoxes().add(new BoxCalculateAmountResponse(1, packageDto.getBoxResponse()));
                            break;
                        }

                    }
                }
                packageDto = new PackageDto();
                packageDto.setVolumenTotal(BigDecimal.valueOf(volMax));
                packageDto.setBoxResponse(createBxResp(boxMax));
                listBoxCalculateResponse.get(listBoxCalculateResponse.size() - 1).getBoxes().add(new BoxCalculateAmountResponse(numBoxMax, packageDto.getBoxResponse()));
            } else {
                for (BoxDto bx : listBoxesDtoSorted) {
                    if (bx.getBoxEntity().getName().equalsIgnoreCase("Rollo") || bx.getBoxEntity().getName().equalsIgnoreCase("Rollo Mini"))
                        continue;
                    if (volumenCorte <= bx.getVolumen().doubleValue()) {
                        packageDto = new PackageDto();
                        packageDto.setBoxResponse(createBxResp(bx));
                        packageDto.setVolumenTotal(BigDecimal.valueOf(volumenCorte));
                        listBoxCalculateResponse.get(listBoxCalculateResponse.size() - 1)
                                .getBoxes().add(new BoxCalculateAmountResponse(1, packageDto.getBoxResponse()));
                        break;
                    }
                }
            }
        }

        BoxServiceImplementation.log.info("Finished calculate the box with the order {}.", calculateBoxRequest.getOrder());

        return listBoxCalculateResponse;
    }

    private void validateNameNotExist(final String name) throws ExistException {
        BoxServiceImplementation.log.info("Starting validate the box if exist {}.", name);
        final var optionalBoxEntity = this.boxRepository.findByNameIgnoreCase(name);
        if (optionalBoxEntity.isPresent()) {
            BoxServiceImplementation.log.error("The box {} exist.", name);
            throw new ExistException(Controller.CATALOG_BOX, "name", name);
        }
    }

    private void validateColorCodeNotExist(final String colorCode) throws ExistException {
        BoxServiceImplementation.log.info("Starting validate the color code if exist {}.", colorCode);
        final var optionalBoxEntity = this.boxRepository.findByColorCodeIgnoreCase(colorCode);
        if (optionalBoxEntity.isPresent()) {
            BoxServiceImplementation.log.error("The color code {} exist.", colorCode);
            throw new ExistException(Controller.CATALOG_BOX, "color.code", colorCode);
        }
    }

    private void validateWidthHeightDepthNotExist(final BigDecimal width, final BigDecimal height, final BigDecimal depth) throws ExistException {
        BoxServiceImplementation.log.info("Starting validate the width, height, depth code if exist {}, {}, {}.", width, height, depth);
        final var optionalBoxEntity = this.boxRepository.findByWidthAndHeightAndDepth(width, height, depth);
        if (optionalBoxEntity.isPresent()) {
            BoxServiceImplementation.log.error("The width, height, depth code if exist {}, {}, {}.", width, height, depth);
            throw new ExistException(Controller.CATALOG_BOX, "width.height.depth", "" + width + ", " + height + ", " + depth);
        }
    }

    private void createBoxHistory(final BoxEntity boxEntity, final UUID admin, final DataBaseActionType dataBaseActionType) {
        try {
            final var boxHistoryEntity = new BoxHistoryEntity();
            final var adminEntity = this.adminHelper.getAdminEntity(admin);
            final var objectMapper = new ObjectMapper();
            final var object = objectMapper.writeValueAsString(boxEntity);
            boxHistoryEntity.setBoxEntity(boxEntity);
            boxHistoryEntity.setAdminEntity(adminEntity);
            boxHistoryEntity.setActionType(dataBaseActionType);
            boxHistoryEntity.setDate(new Date());
            boxHistoryEntity.setObject(object);
            this.boxHistoryRepository.save(boxHistoryEntity);
        } catch (final Exception exception) {
            exception.printStackTrace();
        }
    }



    private double[] splitIntDouble(double amout, double avRoll) {
        int entero = (int) (amout / avRoll);
        double resto = amout % avRoll;
        if (resto > 0) return new double[]{entero, resto};
        return new double[]{entero};
    }

    private double diameTemp(double amout, double diametro, double avRoll) {
        //Cálculo del diámetro de cada corte del rollo
        return (amout * diametro) / avRoll;
    }

    private double volumenRollo(double diametro, double width) {
        return (Math.PI * Math.pow((diametro / 2), 2)) * width;
    }

    private BoxResponse createBxResp(BoxDto bxDto) {
        return new BoxResponse(bxDto.getBoxEntity().getId(), bxDto.getBoxEntity().getName(),
                bxDto.getBoxEntity().getWidth(), bxDto.getBoxEntity().getHeight(), bxDto.getBoxEntity().getDepth(),
                bxDto.getBoxEntity().getColorCode(), bxDto.getBoxEntity().isActive());
    }

    private double tempPeso(double amout, double pXmLin) {
        //Calculo el peso temporal
        //Para sacar el peso en kg
        return (amout * pXmLin) / 1000;
    }
}

@Data
class PackageDto {
    private BigDecimal volumenTotal = BigDecimal.ZERO;
    BoxResponse boxResponse = null;
    private boolean packaging = false;
}
