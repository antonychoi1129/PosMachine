package pos.machine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PosMachine {
    public String printReceipt(List<String> barcodes) {
        return null;
    }

    public List<ReceiptItem> decodeToItems(List<String> barcodes) {
        List<Item> items = ItemDataLoader.loadAllItems();
        Map<String, Integer> barcodesQuantities = new HashMap<>();
        for(String barcode:barcodes){
            if (barcodesQuantities.containsKey(barcode)){
                barcodesQuantities.put(barcode, barcodesQuantities.get(barcode) + 1);
            }
            else{
                barcodesQuantities.put(barcode, 1);
            }
        }
        List<ReceiptItem> receiptItems = new ArrayList<>();
        for (Map.Entry<String, Integer> barcodesQuantity : barcodesQuantities.entrySet()) {
            Item currentItem = items.stream()
                .filter( item -> item.getBarcode().equals(barcodesQuantity.getKey()))
                .findFirst()
                .get();
            int quantity = barcodesQuantity.getValue();
            receiptItems.add(new ReceiptItem(
                currentItem.getName(),
                quantity,
                currentItem.getPrice(),
                quantity*currentItem.getPrice()
            ));
        }
        return receiptItems;
    }

    public Receipt calculateCost(List<ReceiptItem> receiptItems){
        int totalPrice = receiptItems.stream()
            .map( receiptItem -> receiptItem.getSubTotal())
            .reduce( 0, Integer::sum);
        return new Receipt(receiptItems, totalPrice);
    }

    public String generateItemsReceipt(Receipt receipt){
        String itemReceipt = "";
        List<ReceiptItem> receiptItems = receipt.getReceiptItems();
        for(ReceiptItem receiptItem: receiptItems){
            itemReceipt += String.format("Name: %s, Quantity: %d, Unit price: %d (yuan), Subtotal: %d (yuan)\n",
                receiptItem.getName(),
                receiptItem.getQuantity(),
                receiptItem.getUnitPrice(),
                receiptItem.getSubTotal());
        }

        return itemReceipt;
    }
}
