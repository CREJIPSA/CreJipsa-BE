package tave.crezipsa.crezipsa.application.storyboard.dto.request;

public record UpdateStoryboardCutRequest (
	String cutComposition,
	String script,
	String caption,
	String etc
){
}
