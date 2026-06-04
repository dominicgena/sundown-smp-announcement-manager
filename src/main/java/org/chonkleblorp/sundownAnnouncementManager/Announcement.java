package org.chonkleblorp.sundownAnnouncementManager;

import net.kyori.adventure.inventory.Book;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Announcement {
    private String message;
    private Book book;

    public Announcement(String message) {
        setMessage(message);
        createBook(message);
    }

    public void setMessage(String message) {
        this.message = message;
    }
    public void createBook() { this.book = null; }
    public void createBook(String message) {
        if (Objects.equals(message, "")) return;
        List<Component> pages = new ArrayList<>();
        String remainingText = message;
        boolean isFirstPage = true;

        while (!remainingText.isEmpty()) {
            int maxChars = isFirstPage ? 190 : 256;

            // If the remaining text fits entirely on the current page, add it and finish
            if (remainingText.length() <= maxChars) {
                if (isFirstPage) {
                    pages.add(buildFirstPage(remainingText));
                } else {
                    pages.add(Component.text(remainingText));
                }
                break;
            }

            // grab the max possible chunk and find the last space/newline.
            String chunk = remainingText.substring(0, maxChars);
            int lastSpace = chunk.lastIndexOf(' ');
            int lastNewline = chunk.lastIndexOf('\n');

            // pick whichever is furthest down the string
            int splitIndex = Math.max(lastSpace, lastNewline);

            // if there are no spaces, force the split
            if (splitIndex == -1) {
                splitIndex = maxChars;
            }

            // slice out the text for this page
            String pageText = remainingText.substring(0, splitIndex);

            if (isFirstPage) {
                pages.add(buildFirstPage(pageText));
                isFirstPage = false;
            } else {
                pages.add(Component.text(pageText));
            }

            // shrink remainingText for the next loop.
            // if we split on a space, skip that space so the next page doesn't start with an awkward gap
            if (splitIndex < remainingText.length() && remainingText.charAt(splitIndex) == ' ') {
                remainingText = remainingText.substring(splitIndex + 1);
            } else {
                remainingText = remainingText.substring(splitIndex);
            }
        }

        this.book = Book.builder()
                .title(Component.text("Announcement"))
                .author(Component.text("Sundown SMP"))
                .pages(pages)
                .build();
    }

    public String getMessage() { return this.message; }
    public Book getBook() {
        return this.book;
    }

    // helper method to keep the main loop clean
    private static Component buildFirstPage(String text) {
        return Component.text("      Sundown SMP\n\n", NamedTextColor.GOLD)
                .append(Component.text("     Announcement\n\n", NamedTextColor.DARK_PURPLE))
                .append(Component.text(text, NamedTextColor.BLACK));
    }
}
