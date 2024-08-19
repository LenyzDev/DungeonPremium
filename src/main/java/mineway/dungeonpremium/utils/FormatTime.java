package mineway.dungeonpremium.utils;

import java.util.concurrent.TimeUnit;

public class FormatTime {

    private String day = "dia";
    private String hour = "hora";
    private String minute = "minuto";
    private String second = "segundo";
    private String and = "e";

    public String formatBigTime(Long time) {
        if (time >= 86400000L) {
            String duration = String.format("%d "+day,
                    TimeUnit.MILLISECONDS.toDays(time)
            );
            if (TimeUnit.MILLISECONDS.toDays(time) > 1) {
                duration += "s";
            }

            if (time - 86400000 > 3600000L) {
                if (TimeUnit.MILLISECONDS.toHours(time) - TimeUnit.MILLISECONDS.toDays(time) * 24 > 0) {
                    duration += String.format(" "+and+" %d "+hour+"s",
                            TimeUnit.MILLISECONDS.toHours(time) - TimeUnit.MILLISECONDS.toDays(time) * 24
                    );
                }
            }

            return duration;
        } else if (time >= 3600000L) {
            String duration = String.format("%d "+hour,
                    TimeUnit.MILLISECONDS.toHours(time)
            );
            if (TimeUnit.MILLISECONDS.toHours(time) > 1) {
                duration += "s";
            }

            if (time - 3600000L > 60000L) {
                if (TimeUnit.MILLISECONDS.toMinutes(time) - TimeUnit.MILLISECONDS.toHours(time) * 60 > 0) {
                    duration += String.format(" "+and+" %d "+minute+"s",
                            TimeUnit.MILLISECONDS.toMinutes(time) - TimeUnit.MILLISECONDS.toHours(time) * 60
                    );
                } else {
                    if (TimeUnit.MILLISECONDS.toSeconds(time) - TimeUnit.MILLISECONDS.toHours(time) * 3600 > 0) {
                        duration += String.format(" "+and+" %d "+second+"s",
                                TimeUnit.MILLISECONDS.toSeconds(time) - TimeUnit.MILLISECONDS.toHours(time) * 3600
                        );
                    }
                }
            } else {
                if (time - 3600000L > 0L) {
                    duration += String.format(" "+and+" %d "+second+"s",
                            TimeUnit.MILLISECONDS.toSeconds(time) - TimeUnit.MILLISECONDS.toHours(time) * 3600
                    );
                }
            }

            return duration;
        } else {
            if (time >= 60000L) {
                String duration = String.format("%d "+minute,
                        TimeUnit.MILLISECONDS.toMinutes(time)
                );
                if (TimeUnit.MILLISECONDS.toMinutes(time) > 1) {
                    duration += "s";
                }

                if (time - 60000L > 0L) {
                    if (TimeUnit.MILLISECONDS.toSeconds(time) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(time)) > 0) {
                        duration += String.format(" "+and+" %d "+second+"s",
                                TimeUnit.MILLISECONDS.toSeconds(time) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(time))
                        );
                    }
                }
                return duration;
            } else {
                return (time / 1000) + " "+second+"s";
            }
        }
    }
}
