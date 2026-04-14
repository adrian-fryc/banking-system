package pl.mentor.banking.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.mentor.banking.model.dto.TransferRequest;
import pl.mentor.banking.service.TransferService;

@RestController
@RequestMapping("/api/transfers")
@RequiredArgsConstructor
public class TransferController {
    private final TransferService transferService;

//    public TransferController(TransferService transferService){
//        this.transferService = transferService;
//    }

    @PostMapping
    public ResponseEntity<String> performTransfer(@RequestBody TransferRequest request) {
        transferService.transfer(request.getFromUserId(), request.getToUserId(), request.getAmount());
        return ResponseEntity.ok("Przelew przyjęty do realizacji!");
    }
}
